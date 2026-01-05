package com.soul.fin.accounting.read.customer.entity;

import java.time.Instant;
import java.util.UUID;

public record CustomerSummaryV1(UUID aggregateId,
                                UUID eventId,
                                long aggregateVersion,
                                String name,
                                long version,
                                Instant occurredAt) {
}
