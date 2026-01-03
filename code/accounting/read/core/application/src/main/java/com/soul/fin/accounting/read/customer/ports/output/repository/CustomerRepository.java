package com.soul.fin.accounting.read.customer.ports.output.repository;

import com.soul.fin.accounting.read.customer.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CustomerRepository {

    Mono<Customer> findById(UUID id);

    Flux<Customer> findAll();

    Flux<Customer> findBy(int page, int size);

    Mono<Customer> save(Customer customer);

    Mono<Boolean> existsById(UUID id);

    Mono<Void> deleteById(UUID id);

    Mono<Customer> insert(Customer aggregate);

    Mono<Customer> update(Customer aggregate);
}
