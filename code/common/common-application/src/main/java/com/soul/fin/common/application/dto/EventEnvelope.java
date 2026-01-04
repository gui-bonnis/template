package com.soul.fin.common.application.dto;

import com.soul.fin.common.core.event.DomainEvent;
import com.soul.fin.common.core.event.EventMetadata;

import java.time.Instant;
import java.util.UUID;

public record EventEnvelope(UUID eventId,
                            long eventPosition,
                            String eventType,
                            long eventSchemaVersion,
                            UUID aggregateId,
                            String aggregateType,
                            long aggregateVersion,
                            DomainEvent payload,
                            EventMetadata metadata,
                            Instant occurredAt) {
}
