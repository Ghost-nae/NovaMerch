package com.Nova.Merch.Tenant.Service;

import com.Nova.Merch.Access.Model.PlatformRole;
import com.Nova.Merch.Access.Service.AuthorizationService;
import com.Nova.Merch.Tenant.Entity.Tenant;
import com.Nova.Merch.Tenant.Repository.TenantRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class TenantActivationService {

    private final TenantRepository tenantRepository;
    private final AuthorizationService authorizationService;

    public TenantActivationService(
            TenantRepository tenantRepository,
            AuthorizationService authorizationService
    ) {
        this.tenantRepository = tenantRepository;
        this.authorizationService = authorizationService;
    }

    @Transactional
    public Tenant activateTenant(UUID tenantId) {

        authorizationService.requirePlatformRole(
                PlatformRole.PLATFORM_OWNER,
                PlatformRole.PLATFORM_ADMIN
        );

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Tenant not found"
                        )
                );

        if (tenant.getStatus()
                != com.Nova.Merch.Tenant.Model.TenantStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only PENDING tenants can be activated"
            );
        }

        tenant.activate();

        return tenant;
    }
}
