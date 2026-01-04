-- Required for gen_random_uuid()
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE SCHEMA IF NOT EXISTS accounting_states;
CREATE SCHEMA IF NOT EXISTS accounting_references;
CREATE SCHEMA IF NOT EXISTS accounting_events_store;
CREATE SCHEMA IF NOT EXISTS accounting_projections;
CREATE SCHEMA IF NOT EXISTS accounting_integrations;

CREATE TABLE IF NOT EXISTS accounting_states.customer (
    id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name    VARCHAR(255),
    version BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS accounting_events_store.customer (
    global_position BIGSERIAL PRIMARY KEY,

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
    occurred_at TIMESTAMPTZ NOT NULL,

    -- Safety
    UNIQUE (aggregate_id, aggregate_version),
    UNIQUE (event_id)
);

-- Insert test customer data
INSERT INTO accounting_states.customer (id, name, version) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'John Doe', 1),
('550e8400-e29b-41d4-a716-446655440001', 'Jane Smith', 1);


CREATE TABLE IF NOT EXISTS accounting_integrations.customer_outbox (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aggregate_id        UUID NOT NULL,
    event_id            UUID NOT NULL,
    global_position      BIGSERIAL NOT NULL,
    type                VARCHAR(50) NOT NULL,
    payload             JSONB NOT NULL,
    metadata            JSONB NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL,
    processed_at        TIMESTAMPTZ,
    status              VARCHAR(20) NOT NULL
);


CREATE TABLE IF NOT EXISTS accounting_projections.customer_summary_v1 (
    id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name    VARCHAR(255),
    version BIGINT NOT NULL
);

INSERT INTO accounting_projections.customer_summary_v1 (id, name, version) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'John Doe', 1),
('550e8400-e29b-41d4-a716-446655440001', 'Jane Smith', 1);

CREATE TABLE accounting_projections.projection_offset (
    projection_name TEXT PRIMARY KEY,
    last_position   BIGINT NOT NULL
);


CREATE TABLE accounting_projections.poison_event (
    projection_name   TEXT NOT NULL,
    global_position   BIGINT NOT NULL,
    event_id          UUID NOT NULL,
    event_type        TEXT NOT NULL,
    schema_version    INT NOT NULL,
    payload           JSONB NOT NULL,
    metadata          JSONB NOT NULL,
    error_message     TEXT NOT NULL,
    stack_trace       TEXT NOT NULL,
    quarantined_at    TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (projection_name, global_position)
);
