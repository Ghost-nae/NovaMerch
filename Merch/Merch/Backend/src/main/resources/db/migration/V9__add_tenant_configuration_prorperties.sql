ALTER TABLE tenant_configurations

    ADD COLUMN default_language VARCHAR(10)
        NOT NULL DEFAULT 'en',

    ADD COLUMN default_timezone VARCHAR(100)
        NOT NULL DEFAULT 'Africa/Johannesburg',

    ADD COLUMN allow_store_creation BOOLEAN
        NOT NULL DEFAULT TRUE,

    ADD COLUMN allow_custom_domains BOOLEAN
        NOT NULL DEFAULT FALSE;
