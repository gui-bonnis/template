package com.soul.fin.accounting.write.customer.mapper;

import com.soul.fin.accounting.write.customer.entity.Customer;
import com.soul.fin.common.application.mapper.AggregateFactory;
import com.soul.fin.common.core.event.DomainEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;


@Component
public final class CustomerAggregateFactory implements AggregateFactory<Customer> {

    @Override
    public Customer createEmpty() {
        return Customer.builder().build(); // private ctor allowed inside same package
    }

    @Override
    public Customer rehydrate(Flux<DomainEvent> events) {
        Customer customer = createEmpty();

        events.toStream()
                .forEach(customer::when);

        return customer;
    }
}
