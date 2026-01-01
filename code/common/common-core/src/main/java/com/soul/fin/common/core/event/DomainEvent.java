package com.soul.fin.common.core.event;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent extends Message {

    UUID aggregateId();

    long aggregateVersion();

    UUID eventId();

    long eventSchemaVersion();

    Instant occurredAt();
}
