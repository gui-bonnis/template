package com.soul.fin.accounting.data.customer.serializer;

import com.soul.fin.accounting.data.customer.entity.CustomerEventEntity;
import com.soul.fin.common.core.entity.Metadata;
import com.soul.fin.common.core.event.DomainEvent;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
public class CustomerEventSerializer implements EventSerializer {

    private final ObjectMapper objectMapper;

    public CustomerEventSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public CustomerEventEntity serialize(UUID aggregateId,
                                         String aggregateType,
                                         long aggregateVersion,
                                         UUID eventId,
                                         Metadata metadata,
                                         DomainEvent event) {
        return new CustomerEventEntity(
                null,
                aggregateId,
                aggregateType,
                aggregateVersion,
                eventId,
                event.getClass().getSimpleName(),
                event.eventSchemaVersion(),
                objectMapper.writeValueAsString(event),
                objectMapper.writeValueAsString(metadata),
                event.occurredAt()
        );
    }

    @Override
    public <T extends DomainEvent> T deserialize(CustomerEventEntity entity, Class<? extends T> clazz) {

        try {
            return objectMapper.readValue(entity.getPayload(), clazz);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }

    }
}

