package com.soul.fin.server.customer.dto.query;

import java.util.UUID;

public record CustomerQuery(UUID customerId,
                            String name) {
}
