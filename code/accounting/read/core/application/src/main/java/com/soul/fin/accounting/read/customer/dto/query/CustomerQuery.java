package com.soul.fin.accounting.read.customer.dto.query;

import java.util.UUID;

public record CustomerQuery(UUID customerId,
                            String name) {
}
