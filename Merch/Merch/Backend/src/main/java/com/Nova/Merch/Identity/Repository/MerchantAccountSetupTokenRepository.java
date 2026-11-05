package com.Nova.Merch.Identity.Repository;

import com.Nova.Merch.Identity.Entity.MerchantAccountSetupToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MerchantAccountSetupTokenRepository
        extends JpaRepository<MerchantAccountSetupToken, UUID> {

    Optional<MerchantAccountSetupToken> findByTokenHash(
            String tokenHash
    );

    boolean existsByTokenHash(String tokenHash);
}
