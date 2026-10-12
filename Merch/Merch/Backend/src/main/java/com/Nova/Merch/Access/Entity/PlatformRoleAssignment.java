package com.Nova.Merch.Access.Entity;

import com.Nova.Merch.Access.Model.PlatformRole;
import com.Nova.Merch.Common.Model.BaseEntity;
import com.Nova.Merch.Identity.Entity.Identity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "platform_role_assignments",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_platform_role_identity_role",
            columnNames = {"identity_id", "role"}
        )
    }
)
public class PlatformRoleAssignment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "identity_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_platform_role_identity")
    )
    private Identity identity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PlatformRole role;

    public PlatformRoleAssignment(
            Identity identity,
            PlatformRole role
    ) {
        this.identity = Objects.requireNonNull(identity);
        this.role = Objects.requireNonNull(role);
    }
}
