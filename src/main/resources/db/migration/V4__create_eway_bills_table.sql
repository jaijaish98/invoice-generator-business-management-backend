-- Create e-way bills table
CREATE TABLE eway_bills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    bill_number VARCHAR(100) NOT NULL UNIQUE,
    consignor_name VARCHAR(255) NOT NULL,
    consignee_name VARCHAR(255) NOT NULL,
    goods_value DECIMAL(19, 2) NOT NULL,
    transport_mode VARCHAR(50) NOT NULL,
    vehicle_number VARCHAR(50),
    distance_km INTEGER NOT NULL,
    valid_from DATE NOT NULL,
    valid_until DATE NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_eway_bills_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for frequently queried columns
CREATE INDEX idx_eway_bills_user_id ON eway_bills(user_id);
CREATE INDEX idx_eway_bills_bill_number ON eway_bills(bill_number);
CREATE INDEX idx_eway_bills_status ON eway_bills(status);
CREATE INDEX idx_eway_bills_valid_until ON eway_bills(valid_until);

