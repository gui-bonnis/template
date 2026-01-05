package com.soul.fin.accounting.projection.customer.payload;

import java.time.Instant;
import java.util.UUID;

public record CustomerRegisteredPayloadV2(
        UUID aggregateId,
        UUID eventId,
        long aggregateVersion,
        String name,
        long version,
        Instant occurredAt
) {
}