package com.Nova.Merch.Tenant.Repository;

import com.Nova.Merch.Tenant.Entity.TenantConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantConfigurationRepository extends JpaRepository<TenantConfiguration, UUID> {
  Optional<TenantConfiguration> findByTenant_Id(UUID tenantId);
  boolean existsByTenant_Id(UUID tenantId);
}
