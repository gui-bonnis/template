package com.soul.fin.common.projection.model;

import java.time.Instant;
import java.util.UUID;

public record EventEnvelope(UUID eventId,
                            long eventPosition,
                            String eventType,
                            long eventSchemaVersion,
                            UUID aggregateId,
                            String aggregateType,
                            long aggregateVersion,
                            String payload,
                            String metadata,
                            Instant occurredAt) {
}
