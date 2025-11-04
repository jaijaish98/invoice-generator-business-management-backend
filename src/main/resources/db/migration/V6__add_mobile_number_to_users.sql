-- Add mobile number fields to users table
ALTER TABLE users
ADD COLUMN country_code VARCHAR(10),
ADD COLUMN mobile_number VARCHAR(20);

-- Create unique constraint on email and mobile_number combination
CREATE UNIQUE INDEX idx_users_email_mobile ON users(email, mobile_number);

-- Create index on mobile_number for faster lookups
CREATE INDEX idx_users_mobile ON users(mobile_number);

-- Update existing users to have NULL mobile numbers (they can update later)
-- New registrations will require mobile number

