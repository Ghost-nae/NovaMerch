package com.Nova.Merch.Security.Jwt;

import com.Nova.Merch.Identity.Entity.Identity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class JwtService {
  private final JwtEncoder jwtEncoder;
  private final Duration accessTokenExpiration;

  public JwtService(JwtEncoder jwtEncoder, @Value("${jwt.access-token-expiration}") Duration accessTokenExpiration) {
    this.jwtEncoder = jwtEncoder;
    this.accessTokenExpiration;
  }

  public String generateAccessToken(Identity identity) {
    Instant now = Instant.now();

    JwtClaimsSet claims = JwtClaimsSet
      .builder()
      .issuer("novamerch")
      .subject(identity.getId().toString())
      .issueAt(now)
      .expiresAt(now.plus(accessTokenExpiration))
      .id(UUID.randomUUID().toString())
      .claim("email", identity.getEmail())
      .build();
    
    return jwtEncoder
      .encode(JwtEncoderParameters.from(claims))
      .getTokenValue()
  }

  public long getAccessTokenExpirationSeconds() {
    return accessTokenExpiration.toSeconds();
  }
}
