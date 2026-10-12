package com.Nova.Merch.Access.Service;

import com.Nova.Merch.Access.Model.PlatformRole;
import com.Nova.Merch.Access.Model.TenantRole;
import com.Nova.Merch.Security.Service.CurrentIdentityService;
import com.Nova.Merch.Tenant.Entity.Tenant;
import com.Nova.Merch.Tenant.Model.TenantStatus;
import com.Nova.Merch.Tenant.Repository.TenantRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AuthorizationService {

    private final AccessService accessService;
    private final CurrentIdentityService currentIdentityService;
    private final TenantRepository tenantRepository;

    public AuthorizationService(
            AccessService accessService,
            CurrentIdentityService currentIdentityService,
            TenantRepository tenantRepository
    ) {
        this.accessService = accessService;
        this.currentIdentityService = currentIdentityService;
        this.tenantRepository = tenantRepository;
    }

    public UUID requireAuthenticatedIdentity() {
        return currentIdentityService
                .requireActiveIdentity()
                .getId();
    }

    public void requirePlatformRole(PlatformRole... allowedRoles) {

        UUID identityId = requireAuthenticatedIdentity();

        accessService.requirePlatformRole(
                identityId,
                allowedRoles
        );
    }

    public void requireTenantRole(
            UUID tenantId,
            TenantRole... allowedRoles
    ) {

        UUID identityId = requireAuthenticatedIdentity();

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new AccessDeniedException(
                                "Tenant access denied"
                        )
                );

        if (tenant.getStatus() != TenantStatus.ACTIVE) {
            throw new AccessDeniedException(
                    "Tenant is not active"
            );
        }

        accessService.requireTenantRole(
                identityId,
                tenantId,
                allowedRoles
        );
    }
}
