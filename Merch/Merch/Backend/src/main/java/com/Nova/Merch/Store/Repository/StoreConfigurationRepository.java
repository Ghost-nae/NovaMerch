package com.Nova.Merch.Store.Repository;

import com.Nova.Merch.Store.Entity.StoreConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreConfigurationRepository extends JpaRepository<StoreConfiguration, UUID> {
  Optional<StoreConfiguration> findByStore_Id(UUID storeId);
  boolean existsByStore_Id(UUID storeId);
}
