package com.soul.fin.accounting.customer.upcaster;

import com.soul.fin.accounting.customer.entity.CustomerEventEntity;
import com.soul.fin.accounting.customer.serializer.CustomerEventSerializer;
import com.soul.fin.common.core.event.DomainEvent;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventUpcaster {

    private final CustomerEventSerializer serializer;

    public CustomerEventUpcaster(CustomerEventSerializer serializer) {
        this.serializer = serializer;
    }

    public DomainEvent upcast(CustomerEventEntity entity) {

        CustomerUpCaster upCaster;

        return switch (entity.getEventType()) {
            case "CustomerRegistered" -> {
                upCaster = new CustomerRegisteredUpcaster(serializer);
                yield upCaster.upcast(entity);
            }
            case "CustomerRegistrationFailed" -> {
                upCaster = new CustomerRegistrationFailedUpcaster(serializer);
                yield upCaster.upcast(entity);
            }
            case "CustomerValidatedEvent" -> {
                upCaster = new CustomerValidatedEventUpcaster(serializer);
                yield upCaster.upcast(entity);
            }
            default -> null;//throw new InvalidClassException(entity.getEventType());
        };
    }
}

