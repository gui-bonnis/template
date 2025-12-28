package com.soul.fin.accounting.customer.event;

import com.soul.fin.common.core.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record CustomerRegistrationFailed(UUID aggregateId,
                                         long version,
                                         Instant occurredAt,
                                         String reason) implements DomainEvent {


    @Override
    public UUID aggregateId() {
        return aggregateId;
    }

    @Override
    public long aggregateVersion() {
        return version;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }
}