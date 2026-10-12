package com.Nova.Merch.Access.Entity;

import com.Nova.Merch.Common.Model.BaseEntity;
import com.Nova.Merch.Identity.Entity.Identity;
import com.Nova.Merch.Tenant.Entity.Tenant;
import com.Nova.Merch.Access.Model.TenantRole;
import com.Nova.Merch.Access.Model.TenantMembershipStatus;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "tenant_memberships",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_tenant_membership_identity_tenant",
            columnNames = {"identity_id", "tenant_id"}
        )
    }
)
public class TenantMembership extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "identity_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_membership_identity")
    )
    private Identity identity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "tenant_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_membership_tenant")
    )
    private Tenant tenant;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TenantRole role;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TenantMembershipStatus status;

    public TenantMembership(
            Identity identity,
            Tenant tenant,
            TenantRole role
    ) {
        this.identity = java.util.Objects.requireNonNull(identity);
        this.tenant = java.util.Objects.requireNonNull(tenant);
        this.role = java.util.Objects.requireNonNull(role);
        this.status = TenantMembershipStatus.INVITED;
    }
}
