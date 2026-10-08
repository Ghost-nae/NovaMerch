CREATE TABLE store_settings (
    id UUID PRIMARY KEY,
    store_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_store_settings_store
        FOREIGN KEY (store_id)
        REFERENCES stores(id),

    CONSTRAINT uk_store_settings_store
        UNIQUE (store_id)
);
