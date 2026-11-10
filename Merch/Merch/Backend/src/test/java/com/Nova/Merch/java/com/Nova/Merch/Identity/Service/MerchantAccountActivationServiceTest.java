package com.Nova.Merch.Identity.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.InjectMocks;

import com.Nova.Merch.Identity.Repository.MerchantAccountSetupTokenRepository;
import com.Nova.Merch.Identity.Service.AccountSetupTokenService;
import com.Nova.Merch.Identity.Repository.IdentityRepository;
import com.Nova.Merch.Access.Repository.TenantMembershipRepository;
import com.Nova.Merch.Tenant.Repository.TenantRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import com.Nova.Merch.Identity.Entity.Identity;
import com.Nova.Merch.Identity.Entity.MerchantAccountSetupToken;
import com.Nova.Merch.Identity.Model.IdentityStatus;
import com.Nova.Merch.Access.Model.TenantRole;
import com.Nova.Merch.Access.Entity.TenantMembership;
import com.Nova.Merch.Access.Model.TenantMembershipStatus;
import java.util.UUID;
import org.springframework.test.util.ReflectionTestUtils;
import com.Nova.Merch.Tenant.Entity.Tenant;
import com.Nova.Merch.Tenant.Model.TenantStatus;

@ExtendWith(MockitoExtension.class)
class MerchantAccountActivationServiceTest {

    @Mock
    private MerchantAccountSetupTokenRepository tokenRepository;
    
    @Mock
    private AccountSetupTokenService tokenService;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private IdentityRepository identityRepository;
    
    @Mock
    private TenantMembershipRepository membershipRepository;
    
    @Mock
    private TenantRepository tenantRepository;
    
    @InjectMocks
    private MerchantAccountActivationService activationService;
    
    @Test
    void shouldRejectReusedSetupToken() {
        Identity identity = new Identity(
            "merchant@novamerch.com",
            "temporary-password"
        );
        
        Tenant tenant = new Tenant(
            "Nova Test Store",
            "nova-test"
        );
        
        tenant.activate();
        TenantMembership membership = new TenantMembership(
            identity,
            tenant,
            TenantRole.TENANT_OWNER
        );
        ReflectionTestUtils.setField(identity, "id", UUID.randomUUID());
        ReflectionTestUtils.setField(tenant, "id", UUID.randomUUID());
        ReflectionTestUtils.setField(membership, "id", UUID.randomUUID());

        String rawToken = "test-merchant-setup-token";
        String tokenHash = "test-token-hash";
        
        Instant expiresAt = Instant.now().plus(24, ChronoUnit.HOURS);
        
        MerchantAccountSetupToken setupToken =
            new MerchantAccountSetupToken(
                identity,
                membership,
                tokenHash,
                expiresAt
            );
        
        MerchantAccountSetupToken setupToken = new MerchantAccountSetupToken();
    }
}
