package com.Nova.Merch.Access.Repository;

import com.Nova.Merch.Access.Entity.TenantMembership;
import com.Nova.Merch.Access.Model.TenantMembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
