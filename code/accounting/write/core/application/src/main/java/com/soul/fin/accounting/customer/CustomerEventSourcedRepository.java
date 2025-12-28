package com.soul.fin.accounting.customer;

import com.soul.fin.accounting.customer.entity.Customer;
import com.soul.fin.accounting.customer.ports.output.repository.CustomerEventRepository;
import com.soul.fin.accounting.customer.vo.CustomerId;
import com.soul.fin.common.application.AggregateFactory;
import com.soul.fin.common.core.event.Metadata;
import reactor.core.publisher.Mono;

public class CustomerEventSourcedRepository {

    private final CustomerEventRepository eventRepository;
    private final AggregateFactory<Customer> factory;

    public CustomerEventSourcedRepository(CustomerEventRepository eventRepository, AggregateFactory<Customer> factory) {
        this.eventRepository = eventRepository;
        this.factory = factory;
    }

    public Mono<Customer> load(CustomerId id) {
        return Mono.just(factory
                .rehydrate(eventRepository.load(id)));
    }

    public Mono<Void> save(Customer customer, Metadata metadata) {
        return eventRepository.append(
                customer,
                metadata
        );
    }
}

