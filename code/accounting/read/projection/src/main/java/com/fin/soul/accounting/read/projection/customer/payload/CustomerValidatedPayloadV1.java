package com.fin.soul.accounting.read.projection.customer.payload;

import java.util.UUID;

public record CustomerValidatedPayloadV1(
        UUID customerId,
        String name,
        long version
) {
}