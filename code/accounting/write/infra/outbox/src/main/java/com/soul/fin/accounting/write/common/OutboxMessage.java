package com.soul.fin.accounting.common;

import java.time.Instant;
import java.util.UUID;

public record OutboxMessage(
        UUID id,
        String type,
        String payload,
        String metadata,
        Instant occurredAt,
        OutboxStatus status
) {
}
