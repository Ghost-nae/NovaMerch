package com.Nova.Merch.Access.Repository;

import com.Nova.Merch.Access.Entity.PlatformRoleAssignment;
import com.Nova.Merch.Access.Model.PlatformRole;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlatformRoleAssignmentRepository
        extends JpaRepository<PlatformRoleAssignment, UUID> {

    List<PlatformRoleAssignment> findByIdentity_Id(UUID identityId);

    boolean existsByIdentity_IdAndRole(
            UUID identityId,
            PlatformRole role
    );
                
    boolean existsByRole(PlatformRole role);    
}
