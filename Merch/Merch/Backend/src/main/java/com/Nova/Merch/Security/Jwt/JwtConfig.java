package com.Nova.Merch.Security.Jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;


@Configuration
public class JwtConfig {
  @Value("${jwt.private-key}")
  private Resource privateKeyResource;

  @Value("${jwt.public-key}")
  private Resource publicKeyResource;

  @Bean
  public JwtEncoder jwtEncoder() throws Exception {
    RSAPublicKey publicKey = readPublicKey();
    RSAPrivateKey privateKey = readPrivateKey();

    RSAKey rsaKey = new RSAKey.Builder(publicKey)
        .privateKey(privateKey)
        .build();

    JWKset jwkSet = new JWKSet(rsaKey);
    return new NimbusJwtEncoder(
      new ImmutableJWKSet<>(jwkSet)
    );
  }

  @Bean
  public JwtDecoder jwtDecoder() throws Exception {
    return NimbusJwtDecoder
      .withPublicKey(readPublicKey())
      .build();
  }

  private RSAPublicKey readPublicKey() throws Exception {

        String key = readKey(publicKeyResource)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);

        X509EncodedKeySpec keySpec =
                new X509EncodedKeySpec(decoded);

        return (RSAPublicKey) KeyFactory
                .getInstance("RSA")
                .generatePublic(keySpec);
    }

    private RSAPrivateKey readPrivateKey() throws Exception {

        String key = readKey(privateKeyResource)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec keySpec =
                new PKCS8EncodedKeySpec(decoded);

        return (RSAPrivateKey) KeyFactory
                .getInstance("RSA")
                .generatePrivate(keySpec);
    }

    private String readKey(Resource resource) throws Exception {
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes());
        }
    }
}
