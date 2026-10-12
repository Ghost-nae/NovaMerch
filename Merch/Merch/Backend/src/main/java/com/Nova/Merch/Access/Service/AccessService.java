package com.Nova.Merch.Access.Service;

import com.Nova.Merch.Access.Model.PlatformRole;
import com.Nova.Merch.Access.Model.TenantRole;
import com.Nova.Merch.Access.Model.TenantMembershipStatus;
import com.Nova.Merch.Access.Repository.PlatformRoleAssignmentRepository;
import com.Nova.Merch.Access.Repository.TenantMembershipRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AccessService {

    private final PlatformRoleAssignmentRepository platformRoleRepository;
    private final TenantMembershipRepository tenantMembershipRepository;

    public AccessService(
            PlatformRoleAssignmentRepository platformRoleRepository,
            TenantMembershipRepository tenantMembershipRepository
    ) {
        this.platformRoleRepository = platformRoleRepository;
        this.tenantMembershipRepository = tenantMembershipRepository;
    }

    public boolean hasPlatformRole(UUID identityId, PlatformRole role) {
        return platformRoleRepository.existsByIdentity_IdAndRole(
                identityId,
                role
        );
    }

    public boolean hasAnyPlatformRole(
            UUID identityId,
            PlatformRole... roles
    ) {
        return Arrays.stream(roles)
                .anyMatch(role -> hasPlatformRole(identityId, role));
    }

    public void requirePlatformRole(
            UUID identityId,
            PlatformRole... allowedRoles
    ) {
        if (!hasAnyPlatformRole(identityId, allowedRoles)) {
            throw new AccessDeniedException(
                    "Insufficient platform permissions"
            );
        }
    }

    public boolean hasTenantRole(
            UUID identityId,
            UUID tenantId,
            TenantRole... allowedRoles
    ) {
        return tenantMembershipRepository
                .findByIdentity_IdAndTenant_Id(identityId, tenantId)
                .filter(membership ->
                        membership.getStatus() == TenantMembershipStatus.ACTIVE
                )
                .map(membership ->
                        Arrays.asList(allowedRoles)
                                .contains(membership.getRole())
                )
                .orElse(false);
    }

    public void requireTenantRole(
            UUID identityId,
            UUID tenantId,
            TenantRole... allowedRoles
    ) {
        if (!hasTenantRole(identityId, tenantId, allowedRoles)) {
            throw new AccessDeniedException(
                    "Insufficient tenant permissions"
            );
        }
    }
}
