CREATE SCHEMA IF NOT EXISTS customer;

CREATE TABLE IF NOT EXISTS customer.customer (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255)
);

-- Insert test customer data
-- INSERT INTO customer.customer (id, name) VALUES
-- ('550e8400-e29b-41d4-a716-446655440000', 'John Doe'),
-- ('550e8400-e29b-41d4-a716-446655440001', 'Jane Smith');