package com.Nova.Merch.Security.Jwt;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

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

        JWKSet jwkSet = new JWKSet(rsaKey);

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

        byte[] decoded = readPem(
                publicKeyResource,
                "PUBLIC KEY"
        );

        X509EncodedKeySpec keySpec =
                new X509EncodedKeySpec(decoded);

        return (RSAPublicKey) KeyFactory
                .getInstance("RSA")
                .generatePublic(keySpec);
    }

    private RSAPrivateKey readPrivateKey() throws Exception {

        byte[] decoded = readPem(
                privateKeyResource,
                "PRIVATE KEY"
        );

        PKCS8EncodedKeySpec keySpec =
                new PKCS8EncodedKeySpec(decoded);

        return (RSAPrivateKey) KeyFactory
                .getInstance("RSA")
                .generatePrivate(keySpec);
    }

    private byte[] readPem(
            Resource resource,
            String keyType
    ) throws Exception {

        String pem;

        try (InputStream inputStream = resource.getInputStream()) {
            pem = new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }

        String beginMarker = "-----BEGIN " + keyType + "-----";
        String endMarker = "-----END " + keyType + "-----";

        if (!pem.contains(beginMarker) || !pem.contains(endMarker)) {
            throw new IllegalArgumentException(
                    "Invalid PEM format for " + keyType
            );
        }

        String base64Content = pem
                .substring(
                        pem.indexOf(beginMarker) + beginMarker.length(),
                        pem.indexOf(endMarker)
                )
                .replaceAll("\\s", "");

        return Base64.getDecoder().decode(base64Content);
    }
}
