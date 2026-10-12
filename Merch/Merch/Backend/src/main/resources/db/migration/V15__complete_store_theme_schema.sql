
-- Add the missing StoreTheme columns
ALTER TABLE store_themes
    ADD COLUMN primary_color VARCHAR(7)
        NOT NULL DEFAULT '#111111',

    ADD COLUMN secondary_color VARCHAR(7)
        NOT NULL DEFAULT '#FFFFFF',

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

-- Fix the existing accent_color column
UPDATE store_themes
SET accent_color = '#D4AF37'
WHERE accent_color IS NULL;

ALTER TABLE store_themes
    ALTER COLUMN accent_color TYPE VARCHAR(7),
    ALTER COLUMN accent_color SET DEFAULT '#D4AF37',
    ALTER COLUMN accent_color SET NOT NULL;
