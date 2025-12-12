package com.soul.fin.service.customer.adapter;

import com.soul.fin.server.customer.ports.output.repository.CustomerRepository;
import com.soul.fin.service.customer.entity.Customer;
import com.soul.fin.service.customer.mapper.CustomerMapper;
import com.soul.fin.service.customer.repository.CustomerJpaRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerMapper mapper;

    public CustomerRepositoryImpl(CustomerJpaRepository customerJpaRepository, CustomerMapper mapper) {
        this.customerJpaRepository = customerJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Customer> findById(UUID id) {
        return customerJpaRepository.findById(id)
                .map(mapper::toCustomer);
    }

    @Override
    public Flux<Customer> findAll() {
        return customerJpaRepository.findAll()
                .map(mapper::toCustomer);
    }

    @Override
    public Mono<Customer> save(Customer customer) {
        return Mono.just(customer)
                .map(mapper::toCustomerEntity)
                .flatMap(customerJpaRepository::save)
                .map(mapper::toCustomer);
    }
}
