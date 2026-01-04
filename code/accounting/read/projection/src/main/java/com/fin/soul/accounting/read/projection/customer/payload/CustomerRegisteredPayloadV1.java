package com.fin.soul.accounting.read.projection.customer.payload;

import java.util.UUID;

public record CustomerRegisteredPayloadV1(
        UUID customerId,
        String name,
        long version
) {
}