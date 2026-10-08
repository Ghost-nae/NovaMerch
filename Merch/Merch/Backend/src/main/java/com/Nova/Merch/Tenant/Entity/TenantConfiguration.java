package com.Nova.Merch.Tenant.Entity;

import com.Nova.Merch.Common.Model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "default_language", nullable = false, length = 10)
    private String defaultLanguage = "en";

    @Column(name = "default_timezone", nullable = false, length = 100)
    private String defaultTimezone = "Africa/Johannesburg";

    @Column(name = "allow_store_creation", nullable = false)
    private boolean allowStoreCreation = true;

    @Column(name = "allow_custom_domains", nullable = false)
    private boolean allowCustomDomains = false;

    public TenantConfiguration(Tenant tenant) {
        this.tenant = tenant;
    } 
}
