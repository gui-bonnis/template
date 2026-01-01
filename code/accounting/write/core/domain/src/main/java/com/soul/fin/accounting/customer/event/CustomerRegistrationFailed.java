package com.soul.fin.accounting.customer.event;

import com.soul.fin.common.core.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record CustomerRegistrationFailed(UUID aggregateId,
                                         long aggregateVersion,
                                         UUID eventId,
                                         Instant occurredAt,
                                         String reason) implements DomainEvent {

    @Override
    public long eventSchemaVersion() {
        return 1L;
    }
}