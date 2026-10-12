package com.Nova.Merch.Security.Service;

import com.Nova.Merch.Identity.Entity.Identity;
import com.Nova.Merch.Identity.Model.IdentityStatus;
import com.Nova.Merch.Identity.Repository.IdentityRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CurrentIdentityService {

    private final IdentityRepository identityRepository;

    public CurrentIdentityService(IdentityRepository identityRepository) {
        this.identityRepository = identityRepository;
    }

    public UUID requireIdentityId() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new AccessDeniedException("Authentication required");
        }

        try {
            return UUID.fromString(jwt.getSubject());
        } catch (IllegalArgumentException exception) {
            throw new AccessDeniedException("Invalid identity subject");
        }
    }

    public Identity requireActiveIdentity() {
        UUID identityId = requireIdentityId();

        Identity identity = identityRepository.findById(identityId)
                .orElseThrow(() ->
                        new AccessDeniedException("Identity not found")
                );

        if (identity.getStatus() != IdentityStatus.ACTIVE) {
            throw new AccessDeniedException("Identity is not active");
        }

        return identity;
    }
}
