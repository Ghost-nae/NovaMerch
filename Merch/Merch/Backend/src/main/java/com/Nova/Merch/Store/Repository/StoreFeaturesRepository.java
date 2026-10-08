package com.Nova.Merch.Store.Repository;

import com.Nova.Merch.Store.Entity.StoreFeatures;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreFeaturesRepository
        extends JpaRepository<StoreFeatures, UUID> {

    Optional<StoreFeatures> findByStore_Id(UUID storeId);

    boolean existsByStore_Id(UUID storeId);
}
