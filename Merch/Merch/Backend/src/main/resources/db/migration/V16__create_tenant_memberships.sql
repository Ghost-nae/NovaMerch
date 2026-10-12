CREATE TABLE tenant_memberships (
    id UUID PRIMARY KEY,

    identity_id UUID NOT NULL,
    tenant_id UUID NOT NULL,

    role VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_membership_identity
        FOREIGN KEY (identity_id)
        REFERENCES identities(id),

    CONSTRAINT fk_membership_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenants(id),

    CONSTRAINT uk_tenant_membership_identity_tenant
        UNIQUE (identity_id, tenant_id)
);

CREATE INDEX idx_tenant_memberships_tenant_id
    ON tenant_memberships(tenant_id);

CREATE INDEX idx_tenant_memberships_identity_id
    ON tenant_memberships(identity_id);
