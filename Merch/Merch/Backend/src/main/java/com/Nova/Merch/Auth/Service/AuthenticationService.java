package com.Nova.Merch.Auth.Service;

import com.Nova.Merch.Auth.DTO.LoginRequest;
import com.Nova.Merch.Auth.DTO.LoginResponse;
import com.Nova.Merch.Identity.Entity.Identity;
import com.Nova.Merch.Identity.Model.IdentityStatus;
import com.Nova.Merch.Identity.Repository.IdentityRepository;
import com.Nova.Merch.Security.Jwt.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Locale;

@Service
public class AuthenticationService {
  private final IdentityRepository identityRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthenticationService (
    IdentityRepository identityRepository,
    PasswordEncoder passwordEncoder,
    JwtService jwtService
  ) {
    this.identityRepository = identityRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public LoginRequest login(LoginRequest request) {
    String normalizedEmail = normalizedEmail(request.email());
    
  Identity identity = identityRepository
                .findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid email or password")
                );

        validateIdentityStatus(identity);

        if (!passwordEncoder.matches(
                request.password(),
                identity.getPasswordHash()
        )) {
            throw new BadCredentialsException("Invalid email or password");
        }

        identity.setLastLoginAt(Instant.now());
        identityRepository.save(identity);

        String accessToken = jwtService.generateAccessToken(identity);

        return new LoginResponse(
                accessToken,
                "Bearer",
                jwtService.getAccessTokenExpirationSeconds()
        );
    }

    private void validateIdentityStatus(Identity identity) {

        if (identity.getStatus() == IdentityStatus.DISABLED) {
            throw new DisabledException("Account is disabled");
        }

        if (identity.getStatus() == IdentityStatus.LOCKED) {
            throw new LockedException("Account is locked");
        }

        if (identity.getStatus() == IdentityStatus.PENDING_VERIFICATION) {
            throw new DisabledException("Account verification is required");
        }

        if (identity.getStatus() != IdentityStatus.ACTIVE) {
            throw new DisabledException("Account is not active");
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
