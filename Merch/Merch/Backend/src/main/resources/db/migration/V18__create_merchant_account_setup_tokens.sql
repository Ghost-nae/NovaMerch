CREATE TABLE merchant_account_setup_tokens (
    id UUID PRIMARY KEY,
    identity_id UUID NOT NULL,
    token_hash VARCHAR(64) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    used_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_merchant_setup_identity
        FOREIGN KEY (identity_id)
        REFERENCES identities(id)
);

CREATE UNIQUE INDEX idx_merchant_setup_token_hash
    ON merchant_account_setup_tokens(token_hash);

CREATE INDEX idx_merchant_setup_identity
    ON merchant_account_setup_tokens(identity_id);
