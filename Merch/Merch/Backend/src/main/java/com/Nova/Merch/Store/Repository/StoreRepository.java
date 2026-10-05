package com.Nova.Merch.Store.Repository;

import com.Nova.Merch.Store.Entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {
  Optional<Store> findByTenantIdAndCode(UUID tenantId, String code);
  List<Store> findAllByTenantId(UUID tenantId);
  boolean existsByTenantIdAndCode(UUID tenantId, String code);
}
