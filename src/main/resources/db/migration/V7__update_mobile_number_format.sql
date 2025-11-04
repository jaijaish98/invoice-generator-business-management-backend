-- Remove the country_code column as it will be part of mobile_number
ALTER TABLE users DROP COLUMN IF EXISTS country_code;

-- Drop existing index if it exists (PostgreSQL syntax)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_users_mobile') THEN
        DROP INDEX idx_users_mobile;
    END IF;
END $$;

-- Make mobile_number unique
CREATE UNIQUE INDEX idx_users_mobile_unique ON users(mobile_number);

-- Update the composite index to be on email and mobile_number
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_users_email_mobile') THEN
        DROP INDEX idx_users_email_mobile;
    END IF;
END $$;

CREATE INDEX idx_users_email_mobile ON users(email, mobile_number);

