package com.soul.fin.accounting.customer.upcaster;

import com.soul.fin.accounting.customer.entity.CustomerEventEntity;
import com.soul.fin.accounting.customer.event.CustomerRegisteredEvent;
import com.soul.fin.accounting.customer.serializer.CustomerEventSerializer;

public class CustomerRegistrationFailedUpcaster implements CustomerUpCaster {

    private final CustomerEventSerializer serializer;

    public CustomerRegistrationFailedUpcaster(CustomerEventSerializer serializer) {
        this.serializer = serializer;
    }

    public CustomerRegisteredEvent upcast(CustomerEventEntity entity) {

        return switch (entity.getAggregateVersion().toString()) {
            case "1" -> {
                CustomerRegisteredEvent old = serializer.deserialize(entity, CustomerRegisteredEvent.class);

                yield new CustomerRegisteredEvent(
                        old.aggregateId(),
                        old.aggregateVersion(),
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
