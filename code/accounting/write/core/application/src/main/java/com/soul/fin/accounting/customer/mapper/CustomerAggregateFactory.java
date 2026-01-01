package com.soul.fin.accounting.customer.mapper;

import com.soul.fin.accounting.customer.entity.Customer;
import com.soul.fin.common.application.mapper.AggregateFactory;
import com.soul.fin.common.core.event.DomainEvent;
import reactor.core.publisher.Flux;


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
