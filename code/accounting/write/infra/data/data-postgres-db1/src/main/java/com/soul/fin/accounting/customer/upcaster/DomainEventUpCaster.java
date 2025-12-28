package com.soul.fin.accounting.customer.upcaster;

import com.soul.fin.common.core.event.DomainEvent;

public interface DomainEventUpCaster<R extends DomainEvent, T> {
    R upcast(T entity);
}
