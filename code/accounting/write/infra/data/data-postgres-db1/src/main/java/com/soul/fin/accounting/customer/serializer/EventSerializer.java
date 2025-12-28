package com.soul.fin.accounting.customer.serializer;

import com.soul.fin.accounting.customer.entity.CustomerEventEntity;
import com.soul.fin.common.core.event.DomainEvent;
import com.soul.fin.common.core.event.Metadata;

import java.util.UUID;

public interface EventSerializer {

    CustomerEventEntity serialize(
            UUID aggregateId,
            String aggregateType,
            long aggregateVersion,
            UUID eventId,
            Metadata metadata,
            DomainEvent event
    );

    <T extends DomainEvent> T deserialize(CustomerEventEntity entity, Class<? extends T> clazz);
}

