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

    }
}
