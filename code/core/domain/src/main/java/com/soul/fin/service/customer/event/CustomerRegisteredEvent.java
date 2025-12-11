package com.soul.fin.service.customer.event;

import com.soul.fin.common.core.entity.Audit;
import com.soul.fin.common.core.event.DomainEvent;
import com.soul.fin.service.customer.CustomerDOP;

public record CustomerRegisteredEvent(CustomerDOP customerDOP) implements DomainEvent {
    @Override
    public Audit audit() {
        return null;
    }
}