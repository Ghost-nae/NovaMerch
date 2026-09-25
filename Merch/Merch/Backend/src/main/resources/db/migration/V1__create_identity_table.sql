CREATE TABLE identities (
    id UUID PRIMARY KEY,

    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,

    status VARCHAR(30) NOT NULL,

    email_verified BOOLEAN NOT NULL DEFAULT FALSE,

    last_login_at TIMESTAMP WITH TIME ZONE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT uk_identity_email UNIQUE (email),

    CONSTRAINT chk_identity_status CHECK (
        status IN (
            'ACTIVE',
            'DISABLED',
            'LOCKED',
            'PENDING_VERIFICATION'
        )
    )
);
