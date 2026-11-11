package com.Nova.Merch;

import com.Nova.Merch.Access.Entity.TenantMembership;
import com.Nova.Merch.Access.Model.TenantMembershipStatus;
import com.Nova.Merch.Access.Model.TenantRole;
import com.Nova.Merch.Access.Repository.TenantMembershipRepository;
import com.Nova.Merch.Identity.DTO.CompleteMerchantSetupRequest;
import com.Nova.Merch.Identity.Entity.Identity;
import com.Nova.Merch.Identity.Entity.MerchantAccountSetupToken;
import com.Nova.Merch.Identity.Model.IdentityStatus;
import com.Nova.Merch.Identity.Repository.IdentityRepository;
import com.Nova.Merch.Identity.Repository.MerchantAccountSetupTokenRepository;
import com.Nova.Merch.Identity.Service.AccountSetupTokenService;
import com.Nova.Merch.Identity.Service.MerchantAccountActivationService;
import com.Nova.Merch.Tenant.Entity.Tenant;
import com.Nova.Merch.Tenant.Repository.TenantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class MerchantAccountActivationIntegrationTest {

    @Autowired private IdentityRepository identityRepository;
    @Autowired private TenantRepository tenantRepository;
    @Autowired private TenantMembershipRepository membershipRepository;
    @Autowired private MerchantAccountSetupTokenRepository tokenRepository;
    @Autowired private AccountSetupTokenService tokenService;
    @Autowired private MerchantAccountActivationService activationService;
    @Autowired private TransactionTemplate transactionTemplate;

    @Test
    void shouldRejectReusedTokenAfterDatabaseCommit() {
        // Use unique values so this test does not collide with other tests.
        String unique = UUID.randomUUID().toString();
        String email = "merchant-" + unique + "@example.com";

        // Save the merchant, tenant and invitation in a committed transaction.
        UUID membershipId = transactionTemplate.execute(status -> {
            Identity identity = identityRepository.saveAndFlush(
                    new Identity(email, "temporary-password-hash")
            );

            Tenant tenant = new Tenant("Integration Test Store", "test-" + unique);
            tenant.activate();
            tenant = tenantRepository.saveAndFlush(tenant);

            TenantMembership membership = membershipRepository.saveAndFlush(
                    new TenantMembership(identity, tenant, TenantRole.TENANT_OWNER)
            );
            return membership.getId();
        });
        assertNotNull(membershipId);

        // Issue a real setup token, which is stored hashed in PostgreSQL.
        String rawToken = transactionTemplate.execute(status -> {
            TenantMembership membership = membershipRepository.findById(membershipId)
                    .orElseThrow();
            return tokenService.createToken(
                    membership.getIdentity(), membership
            );
        });
        assertNotNull(rawToken);

        String tokenHash = tokenService.hashToken(rawToken);
        UUID tokenId = tokenRepository.findByTokenHash(tokenHash)
                .orElseThrow()
                .getId();

        CompleteMerchantSetupRequest request =
                new CompleteMerchantSetupRequest(rawToken, "SecurePassword123!");

        // The service starts and commits its own transaction.
        activationService.completeSetup(request);

        // Read persisted state after the first activation has committed.
        transactionTemplate.executeWithoutResult(status -> {
            TenantMembership membership = membershipRepository.findById(membershipId)
                    .orElseThrow();
            Identity identity = membership.getIdentity();
            MerchantAccountSetupToken token = tokenRepository.findById(tokenId)
                    .orElseThrow();

            assertEquals(IdentityStatus.ACTIVE, identity.getStatus());
            assertTrue(identity.isEmailVerified());
            assertEquals(TenantMembershipStatus.ACTIVE, membership.getStatus());
            assertTrue(token.isUsed());
            assertNotEquals("temporary-password-hash", identity.getPasswordHash());
        });

        // A separate call/transaction must reject the same token.
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> activationService.completeSetup(request)
        );
        assertEquals("Invalid or expired setup token", exception.getMessage());

        // Confirm the failed replay did not change the committed state.
        transactionTemplate.executeWithoutResult(status -> {
            TenantMembership membership = membershipRepository.findById(membershipId)
                    .orElseThrow();
            Identity identity = membership.getIdentity();
            MerchantAccountSetupToken token = tokenRepository.findById(tokenId)
                    .orElseThrow();

            assertEquals(IdentityStatus.ACTIVE, identity.getStatus());
            assertTrue(identity.isEmailVerified());
            assertEquals(TenantMembershipStatus.ACTIVE, membership.getStatus());
            assertTrue(token.isUsed());
        });
    }

    @Test
    void shouldAllowOnlyOneOfTwoConcurrentActivations() throws Exception {
        String unique = UUID.randomUUID().toString();
        UUID membershipId = transactionTemplate.execute(status -> {
            Identity identity = identityRepository.saveAndFlush(
                    new Identity("concurrent-" + unique + "@example.com", "temporary-password-hash")
            );
            Tenant tenant = new Tenant("Concurrent Test Store", "concurrent-" + unique);
            tenant.activate();
            tenant = tenantRepository.saveAndFlush(tenant);
            return membershipRepository.saveAndFlush(
                    new TenantMembership(identity, tenant, TenantRole.TENANT_OWNER)
            ).getId();
        });
        assertNotNull(membershipId);

        String rawToken = transactionTemplate.execute(status -> {
            TenantMembership membership = membershipRepository.findById(membershipId)
                    .orElseThrow();
            return tokenService.createToken(membership.getIdentity(), membership);
        });
        assertNotNull(rawToken);
        UUID tokenId = tokenRepository.findByTokenHash(tokenService.hashToken(rawToken))
                .orElseThrow().getId();

        CompleteMerchantSetupRequest request =
                new CompleteMerchantSetupRequest(rawToken, "SecurePassword123!");

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            java.util.concurrent.Callable<Boolean> attempt = () -> {
                ready.countDown();
                if (!start.await(10, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("Timed out waiting to start activation");
                }
                try {
                    activationService.completeSetup(request);
                    return true;
                } catch (IllegalArgumentException | IllegalStateException rejected) {
                    return false;
                }
            };

            Future<Boolean> first = executor.submit(attempt);
            Future<Boolean> second = executor.submit(attempt);
            assertTrue(ready.await(10, TimeUnit.SECONDS), "Both requests must be ready");
            start.countDown();

            boolean firstSucceeded = first.get(30, TimeUnit.SECONDS);
            boolean secondSucceeded = second.get(30, TimeUnit.SECONDS);
            assertEquals(1, (firstSucceeded ? 1 : 0) + (secondSucceeded ? 1 : 0),
                    "Exactly one concurrent activation must succeed");
        } finally {
            start.countDown();
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS),
                    "Activation worker threads should terminate");
        }

        transactionTemplate.executeWithoutResult(status -> {
            TenantMembership membership = membershipRepository.findById(membershipId)
                    .orElseThrow();
            Identity identity = membership.getIdentity();
            MerchantAccountSetupToken token = tokenRepository.findById(tokenId)
                    .orElseThrow();
            assertEquals(IdentityStatus.ACTIVE, identity.getStatus());
            assertTrue(identity.isEmailVerified());
            assertEquals(TenantMembershipStatus.ACTIVE, membership.getStatus());
            assertTrue(token.isUsed());
        });
    }
}
