package com.Nova.Merch.Tenant.Service;

import com.Nova.Merch.Access.Model.PlatformRole;
import com.Nova.Merch.Access.Service.AuthorizationService;
import com.Nova.Merch.Tenant.DTO.CreateTenantRequest;
import com.Nova.Merch.Tenant.Entity.Tenant;
import com.Nova.Merch.Tenant.Entity.TenantConfiguration;
import com.Nova.Merch.Tenant.Repository.TenantRepository;
import com.Nova.Merch.Tenant.Repository.TenantConfigurationRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class TenantProvisioningService {

    private final TenantRepository tenantRepository;
    private final TenantConfigurationRepository configurationRepository;
    private final AuthorizationService authorizationService;

    public TenantProvisioningService(
            TenantRepository tenantRepository,
            TenantConfigurationRepository configurationRepository,
            AuthorizationService authorizationService
    ) {
        this.tenantRepository = tenantRepository;
        this.configurationRepository = configurationRepository;
        this.authorizationService = authorizationService;
    }

    @Transactional
    public Tenant provisionTenant(CreateTenantRequest request) {

        authorizationService.requirePlatformRole(
                PlatformRole.PLATFORM_OWNER,
                PlatformRole.PLATFORM_ADMIN
        );

        String name = request.name().trim();
        String code = request.code().trim()
                .toLowerCase(Locale.ROOT);

        if (tenantRepository.existsByCode(code)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Tenant code already exists"
            );
        }

        Tenant tenant = new Tenant(name, code);

        Tenant savedTenant = tenantRepository.save(tenant);

        TenantConfiguration configuration =
                new TenantConfiguration(savedTenant);

        configurationRepository.save(configuration);

        return savedTenant;
    }
}
