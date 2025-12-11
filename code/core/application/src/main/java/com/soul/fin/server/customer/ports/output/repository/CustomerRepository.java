package com.soul.fin.server.customer.ports.output.repository;

import com.soul.fin.service.customer.entity.Customer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public interface CustomerRepository {

    Mono<Customer> findById(UUID id);

    Mono<Customer> save(Customer customer);
}
