package com.Nova.Merch.Tenant.Entity;

import com.Nova.Merch.Common.Model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(
  name = "tenant_configurations",
  uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tenant_configuration_tenant",
                        columnNames = "tenant_id"
                )
        }
)
public class TenantConfiguration extends BaseEntity {

  @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "tenant_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_tenant_configuration_tenant")
    )
  private Tenant tenant;

  protected TenantConfiguration () {}

  public TenantConfiguration (Tenant tenant) {
    this.tenant = tenant;
  }

 public Tenant getTenant() {
   return tenant;
 } 
}
