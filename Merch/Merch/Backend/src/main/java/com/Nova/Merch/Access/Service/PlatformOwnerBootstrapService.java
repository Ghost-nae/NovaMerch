package com.Nova.Merch.Access.Service;

import com.Nova.Merch.Access.Entity.PlatformRoleAssignment;
import com.Nova.Merch.Access.Model.PlatformRole;
import com.Nova.Merch.Access.Repository.PlatformRoleAssignmentRepository;
import com.Nova.Merch.Identity.Entity.Identity;
import com.Nova.Merch.Identity.Model.IdentityStatus;
import com.Nova.Merch.Identity.Repository.IdentityRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class PlatformOwnerBootstrapService {

    private final IdentityRepository identityRepository;
    private final PlatformRoleAssignmentRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${novamerch.bootstrap.owner.email:}")
    private String ownerEmail;

    @Value("${novamerch.bootstrap.owner.password:}")
    private String ownerPassword;

    public PlatformOwnerBootstrapService(
            IdentityRepository identityRepository,
            PlatformRoleAssignmentRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.identityRepository = identityRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void bootstrap() {

        if (roleRepository.existsByRole(PlatformRole.PLATFORM_OWNER)) {
            throw new IllegalStateException(
                    "Platform owner already exists. Bootstrap refused."
            );
        }

        if (ownerEmail == null || ownerEmail.isBlank()
                || ownerPassword == null || ownerPassword.isBlank()) {
            throw new IllegalStateException(
                    "Bootstrap credentials are missing."
            );
        }

        String email = ownerEmail.trim().toLowerCase(Locale.ROOT);

        if (ownerPassword.length() < 16) {
            throw new IllegalStateException(
                    "Bootstrap password must contain at least 16 characters."
            );
        }

        if (identityRepository.existsByEmail(email)) {
            throw new IllegalStateException(
                    "Bootstrap email already belongs to an identity."
            );
        }

        Identity identity = new Identity(
                email,
                passwordEncoder.encode(ownerPassword)
        );

        identity.setStatus(IdentityStatus.ACTIVE);
        identity.setEmailVerified(true);

        Identity savedIdentity = identityRepository.save(identity);

        roleRepository.save(
                new PlatformRoleAssignment(
                        savedIdentity,
                        PlatformRole.PLATFORM_OWNER
                )
        );
    }
}
