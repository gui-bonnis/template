package com.soul.fin.common.application.dto;

import java.time.Instant;
import java.util.UUID;

public record OutboxMessage(
        UUID eventId,
        String type,
        String payloadJson,
        String metadataJson,
        Instant createdAt,
        OutboxStatus status
) {
}
