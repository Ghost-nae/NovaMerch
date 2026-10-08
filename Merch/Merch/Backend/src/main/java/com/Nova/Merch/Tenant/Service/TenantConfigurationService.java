package com.Nova.Merch.Tenant.Service;

import com.Nova.Merch.Tenant.Entity.Tenant;
import com.Nova.Merch.Tenant.Entity.TenantConfiguration;
import com.Nova.Merch.Tenant.Repository.TenantConfigurationRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TenantConfigurationService {

  private final TenantConfigurationRepository tenantConfigurationRepository;

  public TenantConfiguration getConfiguration(UUID tenantId) {
    return tenantConfigurationRepository
      .findByTenant_Id(tenantId)
      .orElseThrow(() -> 
                   new IllegalArgumentException(
                     "Tenant configuration not found: " + tenantId
                   )
    );
  }
  public boolean configurationExists(UUID tenantId) {

    return tenantConfigurationRepository
      .existsByTenant_Id(tenantId);
  }

  @Transactional
  public TenantConfiguration createConfiguration(Tenant tenant) {

    if(tenant == null || tenant.getId() == null) {
      throw new IllegalArgumentException(
        "A persisted tenant is required"
      );
    }

    UUID tenantId = tenant.getId();

    if(configurationExists(tenantId)) {
      throw new IllegalArgumentException(
        "Configuration already exists for tenant: " + tenantId
      );
    }

    TenantConfiguration configuration = new TenantConfiguration(tenant);

    return tenantConfigurationRepository.save(configuration);
  } 
}
