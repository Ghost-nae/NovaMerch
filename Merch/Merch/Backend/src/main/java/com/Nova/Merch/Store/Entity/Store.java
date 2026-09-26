package com.Nova.Merch.Store.Entity;

import com.Nova.Merch.Common.Model.BaseEntity;
import com.Nova.Merch.Store.Model.StoreStatus;
import com.Nova.Merch.Tenant.Entity.Tenant;
import jakarta.persistence.*;

@Entity
@Table(
        name = "stores",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_store_tenant_code",
                        columnNames = {"tenant_id", "code"}
                )
        }
)
public class Store extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "tenant_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_store_tenant")
    )
    private Tenant tenant;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StoreStatus status;

    protected Store() {
    }

    public Store(Tenant tenant, String name, String code) {
        this.tenant = tenant;
        this.name = name;
        this.code = normalizeCode(code);
        this.status = StoreStatus.PENDING;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public StoreStatus getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(StoreStatus status) {
        this.status = status;
    }

    private static String normalizeCode(String code) {
        return code == null
                ? null
                : code.trim().toUpperCase();
    }
}
