package com.soul.fin.accounting.write.data.customer.upcaster;

import com.soul.fin.accounting.write.customer.event.CustomerRegisteredEvent;
import com.soul.fin.accounting.write.data.events.entity.EventsEntity;
import com.soul.fin.accounting.write.data.events.serializer.EventsSerializer;
import org.springframework.stereotype.Component;

@Component
public class CustomerRegisteredUpCaster implements CustomerUpCaster {

    private final EventsSerializer serializer;

    public CustomerRegisteredUpCaster(EventsSerializer serializer) {
        this.serializer = serializer;
    }

    public CustomerRegisteredEvent upcast(EventsEntity entity) {

        return switch (entity.getEventSchemaVersion().toString()) {
            case "1" -> {
                CustomerRegisteredEvent old = serializer.deserialize(entity, CustomerRegisteredEvent.class);

                yield new CustomerRegisteredEvent(
                        old.aggregateId(),
                        old.aggregateVersion(),
                        old.eventId(),
                        old.occurredAt()
                );
            }
            case "2" -> {
                yield serializer.deserialize(entity, CustomerRegisteredEvent.class);
            }
            default -> null;
            //throw new InvalidClassException(eventType);
        };
    }


}
