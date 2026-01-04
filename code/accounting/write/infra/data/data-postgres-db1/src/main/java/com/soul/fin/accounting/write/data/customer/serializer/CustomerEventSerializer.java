package com.soul.fin.accounting.write.data.customer.serializer;

import com.soul.fin.accounting.write.data.customer.entity.CustomerEventEntity;
import com.soul.fin.common.core.entity.Metadata;
import com.soul.fin.common.core.event.DomainEvent;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;

@Component
public class CustomerEventSerializer implements EventSerializer {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public CustomerEventEntity serialize(UUID aggregateId,
                                         String aggregateType,
                                         long aggregateVersion,
                                         UUID eventId,
                                         Metadata metadata,
                                         DomainEvent event,
                                         Instant occurredAt) {
        return new CustomerEventEntity(
                null,
                aggregateId,
                aggregateType,
                aggregateVersion,
                eventId,
                event.getClass().getSimpleName(),
                event.eventSchemaVersion(),
                OBJECT_MAPPER.writeValueAsString(event),
                OBJECT_MAPPER.writeValueAsString(metadata),
                occurredAt
        );
    }

    @Override
    public <T extends DomainEvent> T deserialize(CustomerEventEntity entity, Class<? extends T> clazz) {

        try {
            return OBJECT_MAPPER.readValue(entity.getPayload(), clazz);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }

    }
}

