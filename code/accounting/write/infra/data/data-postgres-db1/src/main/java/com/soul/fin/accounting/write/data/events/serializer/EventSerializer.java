package com.soul.fin.accounting.write.data.events.serializer;

import com.soul.fin.accounting.write.data.events.entity.EventsEntity;
import com.soul.fin.common.core.entity.Metadata;
import com.soul.fin.common.core.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public interface EventSerializer {

    EventsEntity serialize(
            UUID aggregateId,
            String aggregateType,
            long aggregateVersion,
            UUID eventId,
            Metadata metadata,
            DomainEvent event,
            Instant occurredAt
    );

    <T extends DomainEvent> T deserialize(EventsEntity entity, Class<? extends T> clazz);
}

