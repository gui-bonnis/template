package com.soul.fin.common.projection.model;

import java.time.Instant;
import java.util.UUID;

public record ProjectionAck(
        UUID eventId,
        String projectionName,
        Instant processedAt
) {
}

