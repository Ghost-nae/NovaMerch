package com.Nova.Merch.Store.Service;

import com.Nova.Merch.Access.Model.PlatformRole;
import com.Nova.Merch.Access.Service.AuthorizationService;

import com.Nova.Merch.Store.DTO.CreateStoreRequest;
import com.Nova.Merch.Store.Entity.*;
import com.Nova.Merch.Store.Repository.*;

import com.Nova.Merch.Tenant.Entity.Tenant;
import com.Nova.Merch.Tenant.Model.TenantStatus;
import com.Nova.Merch.Tenant.Repository.TenantRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class StoreProvisioningService {

    private final AuthorizationService authorizationService;
    private final TenantRepository tenantRepository;
    private final StoreRepository storeRepository;
    private final StoreConfigurationRepository configurationRepository;
    private final StoreSettingsRepository settingsRepository;
    private final StoreFeaturesRepository featuresRepository;
    private final StoreThemeRepository themeRepository;

    public StoreProvisioningService(
            AuthorizationService authorizationService,
            TenantRepository tenantRepository,
            StoreRepository storeRepository,
            StoreConfigurationRepository configurationRepository,
            StoreSettingsRepository settingsRepository,
            StoreFeaturesRepository featuresRepository,
            StoreThemeRepository themeRepository
    ) {
        this.authorizationService = authorizationService;
        this.tenantRepository = tenantRepository;
        this.storeRepository = storeRepository;
        this.configurationRepository = configurationRepository;
        this.settingsRepository = settingsRepository;
        this.featuresRepository = featuresRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Store provisionStore(
            UUID tenantId,
            CreateStoreRequest request
    ) {

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

        if (tenant.getStatus() != TenantStatus.ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Tenant must be ACTIVE before store provisioning"
            );
        }

        String name = request.name().trim();
        String code = request.code().trim();

        if (storeRepository.existsByTenantIdAndCode(tenantId, code)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Store code already exists for this tenant"
            );
        }

        Store store = storeRepository.saveAndFlush(
                new Store(tenant, name, code)
        );

        configurationRepository.save(
                new StoreConfiguration(store)
        );

        settingsRepository.save(
                new StoreSettings(store)
        );

        featuresRepository.save(
                new StoreFeatures(store)
        );

        themeRepository.save(
                new StoreTheme(store)
        );

        return store;
    }
}
