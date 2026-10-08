
ALTER TABLE store_settings

    ADD COLUMN default_currency VARCHAR(3)
        NOT NULL DEFAULT 'ZAR',

    ADD COLUMN default_language VARCHAR(10)
        NOT NULL DEFAULT 'en',

    ADD COLUMN timezone VARCHAR(100)
        NOT NULL DEFAULT 'Africa/Johannesburg',

    ADD COLUMN order_prefix VARCHAR(20)
        NOT NULL DEFAULT 'ORD',

    ADD COLUMN customer_registration_enabled BOOLEAN
        NOT NULL DEFAULT TRUE,

    ADD COLUMN guest_checkout_enabled BOOLEAN
        NOT NULL DEFAULT FALSE;
