CREATE TABLE tenant_configurations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_tenant_configuration_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenants(id),

    CONSTRAINT uk_tenant_configuration_tenant
        UNIQUE (tenant_id)
);
