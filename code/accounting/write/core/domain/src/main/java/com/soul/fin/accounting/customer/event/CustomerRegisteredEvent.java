package com.soul.fin.accounting.customer.event;

import com.soul.fin.common.core.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record CustomerRegisteredEvent(UUID aggregateId,
                                      long version,
                                      Instant occurredAt) implements DomainEvent {


    @Override
    public UUID aggregateId() {
        return aggregateId;
    }

    @Override
    public long aggregateVersion() {
        return version;
    }

    @Override
    public long eventSchemaVersion() {
        return 1L;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }
}