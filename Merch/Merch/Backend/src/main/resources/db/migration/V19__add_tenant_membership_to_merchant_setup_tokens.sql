ALTER TABLE merchant_account_setup_tokens
ADD COLUMN tenant_membership_id UUID;

ALTER TABLE merchant_account_setup_tokens
ADD CONSTRAINT fk_merchant_setup_membership
FOREIGN KEY (tenant_membership_id)
REFERENCES tenant_memberships(id);

CREATE INDEX idx_merchant_setup_membership
ON merchant_account_setup_tokens(tenant_membership_id);
