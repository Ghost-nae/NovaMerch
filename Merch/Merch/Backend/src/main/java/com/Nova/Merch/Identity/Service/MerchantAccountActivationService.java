package com.Nova.Merch.Identity.Service;

import com.Nova.Merch.Access.Entity.TenantMembership;
import com.Nova.Merch.Access.Model.TenantMembershipStatus;
import com.Nova.Merch.Access.Repository.TenantMembershipRepository;
import com.Nova.Merch.Identity.DTO.CompleteMerchantSetupRequest;
import com.Nova.Merch.Identity.Entity.Identity;
import com.Nova.Merch.Identity.Entity.MerchantAccountSetupToken;
import com.Nova.Merch.Identity.Model.IdentityStatus;
import com.Nova.Merch.Identity.Repository.IdentityRepository;
import com.Nova.Merch.Identity.Repository.MerchantAccountSetupTokenRepository;
import com.Nova.Merch.Tenant.Entity.Tenant;
import com.Nova.Merch.Tenant.Model.TenantStatus;
import com.Nova.Merch.Tenant.Repository.TenantRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MerchantAccountActivationService {

    private final MerchantAccountSetupTokenRepository tokenRepository;
    private final AccountSetupTokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final IdentityRepository identityRepository;
    private final TenantMembershipRepository membershipRepository;
    private final TenantRepository tenantRepository;

    public MerchantAccountActivationService(
            MerchantAccountSetupTokenRepository tokenRepository,
            AccountSetupTokenService tokenService,
            PasswordEncoder passwordEncoder,
            IdentityRepository identityRepository,
            TenantMembershipRepository membershipRepository,
            TenantRepository tenantRepository
    ) {
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.identityRepository = identityRepository;
        this.membershipRepository = membershipRepository;
        this.tenantRepository = tenantRepository;
    }

    @Transactional
    public void completeSetup(CompleteMerchantSetupRequest request) {

        // Step 1: Hash the raw setup token.
        String tokenHash = tokenService.hashToken(request.token());

        // Step 2: Retrieve token reference without locking.
        MerchantAccountSetupToken tokenReference = tokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid or expired setup token"
                        )
                );

        // Step 3: Lock the merchant identity.
        Identity identity = identityRepository
                .findByIdForUpdate(tokenReference.getIdentity().getId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Merchant identity not found"
                        )
                );

        // Step 4: Lock and revalidate the setup token.
        MerchantAccountSetupToken setupToken = tokenRepository
                .findByTokenHashForUpdate(tokenHash)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid or expired setup token"
                        )
                );

        if (!setupToken.isValid()) {
            throw new IllegalArgumentException(
                    "Invalid or expired setup token"
            );
        }

        if (!setupToken.getIdentity().getId().equals(identity.getId())) {
            throw new IllegalStateException(
                    "Setup token identity mismatch"
            );
        }

        // Step 5: Lock the invited tenant membership.
        TenantMembership membership = membershipRepository
                .findByIdForUpdate(
                        setupToken.getTenantMembership().getId()
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Merchant membership not found"
                        )
                );

        // Step 6: Validate membership ownership.
        if (!membership.getIdentity().getId().equals(identity.getId())) {
            throw new IllegalStateException(
                    "Setup token membership mismatch"
            );
        }

        // Step 7: Validate merchant identity status.
        if (identity.getStatus() != IdentityStatus.PENDING_VERIFICATION
                || identity.isEmailVerified()) {
            throw new IllegalStateException(
                    "Account is not eligible for initial setup"
            );
        }

        // Step 8: Validate invitation status.
        if (membership.getStatus() != TenantMembershipStatus.INVITED) {
            throw new IllegalStateException(
                    "Merchant invitation is not active"
            );
        }

        // Step 9: Lock the tenant.
        Tenant tenant = tenantRepository
                .findByIdForUpdate(membership.getTenant().getId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Merchant tenant not found"
                        )
                );

        // Step 10: Validate tenant status.
        if (tenant.getStatus() != TenantStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Merchant tenant is not active"
            );
        }

        // Step 11: Securely encode the merchant password.
        identity.setPasswordHash(
                passwordEncoder.encode(request.password())
        );

        // Step 12: Activate the merchant identity.
        identity.setEmailVerified(true);
        identity.setStatus(IdentityStatus.ACTIVE);

        // Step 13: Activate only the invited membership.
        membership.setStatus(TenantMembershipStatus.ACTIVE);

        // Step 14: Consume the setup token.
        setupToken.markAsUsed();
    }
}
