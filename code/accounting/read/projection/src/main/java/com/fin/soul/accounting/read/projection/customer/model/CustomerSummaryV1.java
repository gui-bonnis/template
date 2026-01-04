package com.fin.soul.accounting.read.projection.customer.model;

import java.time.Instant;
import java.util.UUID;

public record CustomerSummaryV1(UUID id,
                                String name,
                                long version,
                                Instant updatedAt) {
}

