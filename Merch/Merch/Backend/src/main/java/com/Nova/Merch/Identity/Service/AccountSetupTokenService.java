package com.Nova.Merch.Identity.Service;

import com.Nova.Merch.Identity.Entity.Identity;
import com.Nova.Merch.Identity.Entity.MerchantAccountSetupToken;
import com.Nova.Merch.Identity.Repository.MerchantAccountSetupTokenRepository;
import com.Nova.Merch.Access.Entity.TenantMembership;
import com.Nova.Merch.Identity.Repository.IdentityRepository;
import com.Nova.Merch.Identity.Model.IdentityStatus;

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
    private final SecureRandom secureRandom = new SecureRandom();
    private final IdentityRepository identityRepository;

    public AccountSetupTokenService(
            MerchantAccountSetupTokenRepository tokenRepository,
            IdentityRepository identityRepository
    ) {
        this.tokenRepository = tokenRepository;
        this.identityRepository = identityRepository;
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

        // Step 3: Validate membership ownership.
        if (!tenantMembership.getIdentity().getId()
                .equals(lockedIdentity.getId())) {
            throw new IllegalStateException(
                    "Merchant membership does not belong to identity"
            );
        }

        // Step 4: Revoke previous unused setup tokens.
        tokenRepository
                .findUnusedByMembershipIdForUpdate(
                        tenantMembership.getId()
                )
                .forEach(MerchantAccountSetupToken::revoke);

        // Step 5: Generate a secure random token.
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String rawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        // Step 6: Hash the token before persistence.
        String tokenHash = hashToken(rawToken);

        // Step 7: Create a new setup token.
        MerchantAccountSetupToken setupToken =
                new MerchantAccountSetupToken(
                        lockedIdentity,
                        tenantMembership,
                        tokenHash,
                        Instant.now().plus(24, ChronoUnit.HOURS)
                );

        // Step 8: Save the token.
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
