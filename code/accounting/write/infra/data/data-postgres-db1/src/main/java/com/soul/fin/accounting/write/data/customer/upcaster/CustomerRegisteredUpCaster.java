package com.soul.fin.accounting.write.data.customer.upcaster;

import com.soul.fin.accounting.write.customer.event.CustomerRegisteredEvent;
import com.soul.fin.accounting.write.data.customer.entity.CustomerEventEntity;
import com.soul.fin.accounting.write.data.customer.serializer.CustomerEventSerializer;
import org.springframework.stereotype.Component;

@Component
public class CustomerRegisteredUpCaster implements CustomerUpCaster {

    private final CustomerEventSerializer serializer;

    public CustomerRegisteredUpCaster(CustomerEventSerializer serializer) {
        this.serializer = serializer;
    }

    public CustomerRegisteredEvent upcast(CustomerEventEntity entity) {

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
