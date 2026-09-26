package com.Nova.Merch.Tenant.Entity;

import com.Nova.Merch.Common.Model.BaseEntity;
import com.Nova.Merch.Tenant.Model.TenantStatus;
import jakarta.persistence.*;

@Entity
@Table(
  name = "Tenants",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uk_tenant_code",
      columnNames = "code"
    )
  }
)
public class Tenant extends BaseEntity {

  @Column(nullable = false, length = 150)
  private String name;

  @Coilumn(nullable = false, length = 50)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private TenantStatus status;

  protected Tenant() {}

  public Tenant(String name, String code) {
    this.name = name;
    this.code = normalizeCode(code);
    this.status = TenantStatus.PENDING;
  }
}
