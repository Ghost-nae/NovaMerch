package com.Nova.Merch.Identity.Entity;

import com.Nova.Merch.Common.Model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(
    name = "merchant_account_setup_tokens",
    indexes = {
        @Index(
            name = "idx_merchant_setup_token_hash",
            columnList = "token_hash",
            unique = true
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MerchantAccountSetupToken extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "identity_id", nullable = false)
    private Identity identity;

    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "used_at")
    private Instant usedAt;

    public MerchantAccountSetupToken(
            Identity identity,
            String tokenHash,
            Instant expiresAt
    ) {
        this.identity = Objects.requireNonNull(identity);
        this.tokenHash = Objects.requireNonNull(tokenHash);
        this.expiresAt = Objects.requireNonNull(expiresAt);
    }

    public boolean isExpired() {
        return !Instant.now().isBefore(expiresAt);
    }

    public boolean isUsed() {
        return usedAt != null;
    }

    public boolean isValid() {
        return !isExpired() && !isUsed();
    }

    public void markAsUsed() {
        if (!isValid()) {
            throw new IllegalStateException(
                "Account setup token is invalid or expired"
            );
        }

        this.usedAt = Instant.now();
    }
}
