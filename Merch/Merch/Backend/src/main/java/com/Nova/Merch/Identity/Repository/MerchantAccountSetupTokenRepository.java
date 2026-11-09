package com.Nova.Merch.Identity.Repository;

import com.Nova.Merch.Identity.Entity.MerchantAccountSetupToken;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;
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
 
 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @Query("""
        SELECT token
        FROM MerchantAccountSetupToken token
        WHERE token.tenantMembership.id = :membershipId
          AND token.usedAt IS NULL
          ORDER BY token.id
 """)
List<MerchantAccountSetupToken> findUnusedByMembershipIdForUpdate(
        @Param("membershipId") UUID membershipId
);
            
}
