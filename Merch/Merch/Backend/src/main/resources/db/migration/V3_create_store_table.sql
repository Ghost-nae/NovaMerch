CREATE TABLE stores (
    id UUID PRIMARY KEY,

    tenant_id UUID NOT NULL,

    name VARCHAR(150) NOT NULL,
    code VARCHAR(50) NOT NULL,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_store_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenants(id),

    CONSTRAINT uk_store_tenant_code
        UNIQUE (tenant_id, code),

    CONSTRAINT chk_store_status
        CHECK (
            status IN (
                'PENDING',
                'ACTIVE',
                'SUSPENDED',
                'DEACTIVATED'
            )
        )
);
