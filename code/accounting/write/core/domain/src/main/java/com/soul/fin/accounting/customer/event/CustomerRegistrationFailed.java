package com.soul.fin.accounting.customer.event;

import com.soul.fin.common.core.entity.Audit;
import com.soul.fin.common.core.event.DomainEvent;
import com.soul.fin.accounting.customer.CustomerDOP;

public record CustomerRegistrationFailed(CustomerDOP customerDOP,
                                         String reason) implements DomainEvent {
    @Override
    public Audit audit() {
        return null;
    }
}
