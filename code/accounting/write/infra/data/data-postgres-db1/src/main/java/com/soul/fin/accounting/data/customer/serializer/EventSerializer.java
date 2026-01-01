package com.soul.fin.accounting.data.customer.serializer;

import com.soul.fin.accounting.data.customer.entity.CustomerEventEntity;
import com.soul.fin.common.core.entity.Metadata;
import com.soul.fin.common.core.event.DomainEvent;

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

