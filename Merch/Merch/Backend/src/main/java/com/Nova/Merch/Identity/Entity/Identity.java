package com.Nova.Merch.Identity.Entity

import com.Nova.Merch.Common.Model.BaseEntity;
import com.Nova.Merch.Identity.Model.IdentityStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
  name = "identities",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uk_identity_email",
      columnNames = "email"
    )
  }
)
public class Identity extends BaseEntity {
  @Column(nullable = false, length = 255)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private IdentityStatus status;

  @Column(name = "email_verified", nullable = false)
  private boolean emailVerified = false;

  @Column(name = "last_login_at")
  private Instant lastLoginAt;

  protected Identity () {}

  public Identity(String email, String passwordHash) {
        this.email = normalizeEmail(email);
        this.passwordHash = passwordHash;
        this.status = IdentityStatus.PENDING_VERIFICATION;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public IdentityStatus getStatus() {
        return status;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public void setEmail(String email) {
        this.email = normalizeEmail(email);
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setStatus(IdentityStatus status) {
        this.status = status;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void setLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    private static String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
