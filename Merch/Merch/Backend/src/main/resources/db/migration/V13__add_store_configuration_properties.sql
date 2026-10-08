ALTER TABLE store_configurations

    ADD COLUMN configuration_version INTEGER
        NOT NULL DEFAULT 1,

    ADD COLUMN configuration_completed BOOLEAN
        NOT NULL DEFAULT FALSE,

    ADD COLUMN last_published_at TIMESTAMP WITH TIME ZONE;
