package com.Nova.Merch.Store.Repository;

import com.Nova.Merch.Store.Entity.StoreTheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreThemeRepository
        extends JpaRepository<StoreTheme, UUID> {

    Optional<StoreTheme> findByStore_Id(UUID storeId);

    boolean existsByStore_Id(UUID storeId);
}
