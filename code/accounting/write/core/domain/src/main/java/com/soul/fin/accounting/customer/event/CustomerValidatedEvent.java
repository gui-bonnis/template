package com.soul.fin.accounting.customer.event;

import com.soul.fin.common.core.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record CustomerValidatedEvent(UUID aggregateId,
                                     long aggregateVersion,
                                     UUID eventId,
                                     Instant occurredAt) implements DomainEvent {

    @Override
    public long eventSchemaVersion() {
        return 1L;
    }
}