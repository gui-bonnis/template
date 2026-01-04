package com.soul.fin.accounting.projection.customer.payload;

import java.util.UUID;

public record CustomerRegisteredPayloadV1(
        UUID customerId,
        String name,
        long version
) {
}