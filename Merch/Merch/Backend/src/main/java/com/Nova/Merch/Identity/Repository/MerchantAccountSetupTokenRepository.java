package com.Nova.Merch.Identity.Repository;

import com.Nova.Merch.Identity.Entity.MerchantAccountSetupToken;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MerchantAccountSetupTokenRepository
        extends JpaRepository<MerchantAccountSetupToken, UUID> {

    Optional<MerchantAccountSetupToken> findByTokenHash(
            String tokenHash
    );

    boolean existsByTokenHash(String tokenHash);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT token
        FROM MerchantAccountSetupToken token
        WHERE token.tokenHash = :tokenHash
    """)
    Optional<MerchantAccountSetupToken> findByTokenHashForUpdate(
            @Param("tokenHash") String tokenHash
    );
}
