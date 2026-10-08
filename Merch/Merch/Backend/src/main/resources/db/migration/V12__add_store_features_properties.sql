ALTER TABLE store_features

    ADD COLUMN ecommerce_enabled BOOLEAN
        NOT NULL DEFAULT TRUE,

    ADD COLUMN online_payments_enabled BOOLEAN
        NOT NULL DEFAULT FALSE,

    ADD COLUMN delivery_enabled BOOLEAN
        NOT NULL DEFAULT FALSE,

    ADD COLUMN pickup_enabled BOOLEAN
        NOT NULL DEFAULT FALSE,

    ADD COLUMN reviews_enabled BOOLEAN
        NOT NULL DEFAULT FALSE,

    ADD COLUMN discounts_enabled BOOLEAN
        NOT NULL DEFAULT FALSE,

    ADD COLUMN inventory_enabled BOOLEAN
        NOT NULL DEFAULT TRUE,

    ADD COLUMN subscriptions_enabled BOOLEAN
        NOT NULL DEFAULT FALSE;
