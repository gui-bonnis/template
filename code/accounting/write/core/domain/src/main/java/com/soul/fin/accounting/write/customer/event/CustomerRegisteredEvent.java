package com.soul.fin.accounting.write.customer.event;

import com.soul.fin.common.core.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record CustomerRegisteredEvent(UUID aggregateId,
                                      long aggregateVersion,
                                      UUID eventId,
                                      String name,
                                      long version,
                                      Instant occurredAt) implements DomainEvent {


    @Override
    public long eventSchemaVersion() {
        return 1L;
    }
}