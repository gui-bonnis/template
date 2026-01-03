package com.soul.fin.accounting.write.customer.vo;

import com.soul.fin.common.core.vo.BaseId;

import java.util.UUID;

public class CustomerId extends BaseId<UUID> {
    public CustomerId(UUID value) {
        super(value);
        if (value == null) throw new IllegalArgumentException("ID cannot be null");
        if (value.equals(new UUID(0, 0))) throw new IllegalArgumentException("ID cannot be zero UUID");
    }
}
