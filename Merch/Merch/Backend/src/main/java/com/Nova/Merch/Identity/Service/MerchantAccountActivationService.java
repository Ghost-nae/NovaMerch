package com.Nova.Merch.Identity.Service;

import com.Nova.Merch.Access.Entity.TenantMembership;
import com.Nova.Merch.Access.Model.TenantMembershipStatus;
import com.Nova.Merch.Identity.DTO.CompleteMerchantSetupRequest;
import com.Nova.Merch.Identity.Entity.Identity;
import com.Nova.Merch.Identity.Entity.MerchantAccountSetupToken;
import com.Nova.Merch.Identity.Model.IdentityStatus;
import com.Nova.Merch.Identity.Repository.MerchantAccountSetupTokenRepository;
import com.Nova.Merch.Identity.Repository.IdentityRepository;
import com.Nova.Merch.Tenant.Model.TenantStatus;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MerchantAccountActivationService {

    private final MerchantAccountSetupTokenRepository tokenRepository;
    private final AccountSetupTokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final IdentityRepository identityRepository;

    public MerchantAccountActivationService(
            MerchantAccountSetupTokenRepository tokenRepository,
            AccountSetupTokenService tokenService,
            PasswordEncoder passwordEncoder, IdentityRepository identityRepository
    ) {
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.identityRepository = identityRepository;
    }

    
    @Transactional
    public void completeSetup(CompleteMerchantSetupRequest request) {
    
        String tokenHash = tokenService.hashToken(request.token());
    
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
    
        Identity identity = identityRepository
                .findByIdForUpdate(setupToken.getIdentity().getId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Merchant identity not found"
                        )
                );
    
        TenantMembership membership = setupToken.getTenantMembership();
    
        if (!membership.getIdentity().getId().equals(identity.getId())) {
            throw new IllegalStateException(
                    "Setup token membership mismatch"
            );
        }
    
        if (identity.getStatus() != IdentityStatus.PENDING_VERIFICATION
                || identity.isEmailVerified()) {
            throw new IllegalStateException(
                    "Account is not eligible for initial setup"
            );
        }
    
        if (membership.getStatus() != TenantMembershipStatus.INVITED) {
            throw new IllegalStateException(
                    "Merchant invitation is not active"
            );
        }
    
        if (membership.getTenant().getStatus() != TenantStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Merchant tenant is not active"
            );
        }
    
        identity.setPasswordHash(
                passwordEncoder.encode(request.password())
        );
    
        identity.setEmailVerified(true);
        identity.setStatus(IdentityStatus.ACTIVE);
    
        membership.setStatus(TenantMembershipStatus.ACTIVE);
    
        setupToken.markAsUsed();
    }

}
