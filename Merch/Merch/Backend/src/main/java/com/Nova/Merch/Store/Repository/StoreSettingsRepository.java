package com.Nova.Merch.Store.Repository;

import com.Nova.Merch.Store.Entity.StoreSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreSettingsRepository extends JpaRepository<StoreSettings, UUID> {
  Optional<StoreSettings> findByStore_Id(UUID storeId);
  boolean existsByStore_Id(UUID storeId);
}
