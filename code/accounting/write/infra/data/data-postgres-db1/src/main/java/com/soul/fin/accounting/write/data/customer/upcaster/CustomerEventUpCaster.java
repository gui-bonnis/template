package com.soul.fin.accounting.write.data.customer.upcaster;

import com.soul.fin.accounting.write.data.customer.entity.CustomerEventEntity;
import com.soul.fin.accounting.write.data.customer.serializer.CustomerEventSerializer;
import com.soul.fin.common.core.event.DomainEvent;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventUpCaster {

    private final CustomerEventSerializer serializer;

    public CustomerEventUpCaster(CustomerEventSerializer serializer) {
        this.serializer = serializer;
    }

    public DomainEvent upcast(CustomerEventEntity entity) {

        CustomerUpCaster upCaster;

        return switch (entity.getEventType()) {
            case "CustomerRegistered" -> {
                upCaster = new CustomerRegisteredUpCaster(serializer);
                yield upCaster.upcast(entity);
            }
            case "CustomerRegistrationFailed" -> {
                upCaster = new CustomerRegistrationFailedUpCaster(serializer);
                yield upCaster.upcast(entity);
            }
            case "CustomerValidatedEvent" -> {
                upCaster = new CustomerValidatedEventUpCaster(serializer);
                yield upCaster.upcast(entity);
            }
            default -> null;//throw new InvalidClassException(entity.getEventType());
        };
    }
}

