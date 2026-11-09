package com.Nova.Merch.Identity.Service;

import com.Nova.Merch.Access.Entity.TenantMembership;
import com.Nova.Merch.Access.Model.TenantMembershipStatus;
import com.Nova.Merch.Access.Repository.TenantMembershipRepository;
import com.Nova.Merch.Identity.Entity.Identity;
import com.Nova.Merch.Identity.Entity.MerchantAccountSetupToken;
import com.Nova.Merch.Identity.Model.IdentityStatus;
import com.Nova.Merch.Identity.Repository.IdentityRepository;
import com.Nova.Merch.Identity.Repository.MerchantAccountSetupTokenRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class AccountSetupTokenService {

    private final MerchantAccountSetupTokenRepository tokenRepository;
    private final IdentityRepository identityRepository;
    private final TenantMembershipRepository membershipRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    public AccountSetupTokenService(
            MerchantAccountSetupTokenRepository tokenRepository,
            IdentityRepository identityRepository,
            TenantMembershipRepository membershipRepository
    ) {
        this.tokenRepository = tokenRepository;
        this.identityRepository = identityRepository;
        this.membershipRepository = membershipRepository;
    }

    @Transactional
    public String createToken(
            Identity identity,
            TenantMembership tenantMembership
    ) {

        // Step 1: Lock the merchant identity.
        Identity lockedIdentity = identityRepository
                .findByIdForUpdate(identity.getId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Merchant identity not found"
                        )
                );

        // Step 2: Validate identity eligibility.
        if (lockedIdentity.getStatus() != IdentityStatus.PENDING_VERIFICATION
                || lockedIdentity.isEmailVerified()) {
            throw new IllegalStateException(
                    "Merchant identity is not eligible for initial setup"
            );
        }

        // Step 3: Lock the tenant membership.
        TenantMembership lockedMembership = membershipRepository
                .findByIdForUpdate(tenantMembership.getId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Merchant membership not found"
                        )
                );

        // Step 4: Validate membership ownership.
        if (!lockedMembership.getIdentity().getId()
                .equals(lockedIdentity.getId())) {
            throw new IllegalStateException(
                    "Merchant membership does not belong to identity"
            );
        }

        // Step 5: Validate membership status.
        if (lockedMembership.getStatus() != TenantMembershipStatus.INVITED) {
            throw new IllegalStateException(
                    "Merchant membership is not eligible for setup"
            );
        }

        // Step 6: Revoke previous unused setup tokens.
        tokenRepository
                .findUnusedByMembershipIdForUpdate(
                        lockedMembership.getId()
                )
                .forEach(MerchantAccountSetupToken::revoke);

        // Step 7: Generate a secure random token.
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String rawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        // Step 8: Hash the token before persistence.
        String tokenHash = hashToken(rawToken);

        // Step 9: Create the replacement setup token.
        MerchantAccountSetupToken setupToken =
                new MerchantAccountSetupToken(
                        lockedIdentity,
                        lockedMembership,
                        tokenHash,
                        Instant.now().plus(24, ChronoUnit.HOURS)
                );

        // Step 10: Persist the setup token.
        tokenRepository.save(setupToken);

        // Return the raw token for secure invitation delivery.
        return rawToken;
    }

    public String hashToken(String rawToken) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    rawToken.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 is unavailable",
                    exception
            );
        }
    }
}
