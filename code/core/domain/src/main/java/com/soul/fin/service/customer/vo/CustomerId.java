package com.soul.fin.service.customer.vo;

import java.util.UUID;

public record CustomerId(UUID value) {
    public CustomerId {
        if (value == null) throw new IllegalArgumentException("ID cannot be null");
        if (value.equals(new UUID(0, 0))) throw new IllegalArgumentException("ID cannot be zero UUID");
    }
}
