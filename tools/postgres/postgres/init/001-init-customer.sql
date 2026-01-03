-- Required for gen_random_uuid()
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE SCHEMA IF NOT EXISTS customer;

CREATE TABLE IF NOT EXISTS customer.customer (
    id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name    VARCHAR(255),
    version BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS customer.event_store_customer (
    event_position BIGSERIAL PRIMARY KEY,

    -- Aggregate identity
    aggregate_id UUID NOT NULL,
    aggregate_type TEXT NOT NULL,

    -- Versioning (per aggregate)
    aggregate_version BIGINT NOT NULL,

    -- Event identity
    event_id UUID NOT NULL,
    event_type TEXT NOT NULL,

    -- Versioning (event schema)
    event_schema_version BIGINT NOT NULL,

    -- Event payload
    payload JSONB NOT NULL,
    metadata JSONB NOT NULL,

    -- Time & tracing
    occurred_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    -- Safety
    UNIQUE (aggregate_id, aggregate_version),
    UNIQUE (event_id)
);

-- Insert test customer data
INSERT INTO customer.customer (id, name, version) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'John Doe', 1),
('550e8400-e29b-41d4-a716-446655440001', 'Jane Smith', 1);

CREATE SCHEMA IF NOT EXISTS outbox;

CREATE TABLE IF NOT EXISTS outbox.customer (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aggregate_id        UUID NOT NULL,
    event_id            UUID NOT NULL,
    event_position      BIGSERIAL NOT NULL,
    type                VARCHAR(50) NOT NULL,
    payload             JSONB NOT NULL,
    metadata            JSONB NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL,
    processed_at        TIMESTAMPTZ,
    status              VARCHAR(20) NOT NULL
);
