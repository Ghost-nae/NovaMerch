CREATE TABLE platform_role_assignments (
    id UUID PRIMARY KEY,

    identity_id UUID NOT NULL,
    role VARCHAR(30) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_platform_role_identity
        FOREIGN KEY (identity_id)
        REFERENCES identities(id),

    CONSTRAINT uk_platform_role_identity_role
        UNIQUE (identity_id, role),

    CONSTRAINT chk_platform_role
        CHECK (role IN (
            'PLATFORM_OWNER',
            'PLATFORM_ADMIN',
            'PLATFORM_SUPPORT'
        ))
);

CREATE INDEX idx_platform_role_identity_id
    ON platform_role_assignments(identity_id);
