package com.Nova.Merch.Access.Repository;

import com.Nova.Merch.Access.Entity.TenantMembership;
import com.Nova.Merch.Access.Model.TenantMembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantMembershipRepository
        extends JpaRepository<TenantMembership, UUID> {

    Optional<TenantMembership> findByIdentity_IdAndTenant_Id(
            UUID identityId,
            UUID tenantId
    );

    boolean existsByIdentity_IdAndTenant_Id(
            UUID identityId,
            UUID tenantId
    );

    List<TenantMembership> findByTenant_Id(UUID tenantId);

    List<TenantMembership> findByIdentity_IdAndStatus(
            UUID identityId,
            TenantMembershipStatus status
    );

   
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("""
            SELECT membership
            FROM TenantMembership membership
            WHERE membership.id = :membershipId
        """)
        Optional<TenantMembership> findByIdForUpdate(
                @Param("membershipId") UUID membershipId
        );
             
}
