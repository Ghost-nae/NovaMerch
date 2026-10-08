
ALTER TABLE store_themes

    ADD COLUMN primary_color VARCHAR(7)
        NOT NULL DEFAULT '#111111',

    ADD COLUMN secondary_color VARCHAR(7)
        NOT NULL DEFAULT '#FFFFFF',

    ADD COLUMN accent_color VARCHAR(7)
        NOT NULL DEFAULT '#D4AF37',

    ADD COLUMN background_color VARCHAR(7)
        NOT NULL DEFAULT '#FFFFFF',

    ADD COLUMN text_color VARCHAR(7)
        NOT NULL DEFAULT '#111111',

    ADD COLUMN font_family VARCHAR(100)
        NOT NULL DEFAULT 'Inter',

    ADD COLUMN logo_url VARCHAR(2048),

    ADD COLUMN banner_url VARCHAR(2048),

    ADD COLUMN template VARCHAR(50)
        NOT NULL DEFAULT 'MODERN',

    ADD COLUMN button_style VARCHAR(50)
        NOT NULL DEFAULT 'ROUNDED';
