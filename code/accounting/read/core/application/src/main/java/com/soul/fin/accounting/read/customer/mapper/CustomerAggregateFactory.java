package com.soul.fin.accounting.read.customer.mapper;


import com.soul.fin.accounting.read.customer.entity.Customer;
import com.soul.fin.common.application.mapper.AggregateFactory;
import com.soul.fin.common.core.event.DomainEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public final class CustomerAggregateFactory implements AggregateFactory<Customer> {

    //TODO remove later, it is just a Bean problem temporary being fixed

    @Override
    public Customer createEmpty() {
        return Customer.builder().build();
    }

    @Override
    public Customer rehydrate(Flux<DomainEvent> events) {
        Customer customer = createEmpty();

        events.toStream()
                .forEach(customer::when);

        return customer;
    }
}
