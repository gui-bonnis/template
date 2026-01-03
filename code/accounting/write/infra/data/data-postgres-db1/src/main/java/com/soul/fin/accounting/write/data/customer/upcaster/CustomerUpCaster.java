package com.soul.fin.accounting.write.data.customer.upcaster;

import com.soul.fin.accounting.write.data.customer.entity.CustomerEventEntity;
import com.soul.fin.common.core.event.DomainEvent;

public interface CustomerUpCaster extends DomainEventUpCaster
        <DomainEvent, CustomerEventEntity> {
}
