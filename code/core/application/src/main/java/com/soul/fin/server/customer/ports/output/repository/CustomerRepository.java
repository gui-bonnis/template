package com.soul.fin.server.customer.ports.output.repository;

import com.soul.fin.service.customer.entity.Customer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public interface CustomerRepository {

    Mono<Customer> findById(UUID id);

    Flux<Customer> findAll();

    Flux<Customer> findBy(int page, int size);

    Mono<Customer> save(Customer customer);

    Mono<Boolean> existsById(UUID id);

    Mono<Void> deleteById(UUID id);
}
