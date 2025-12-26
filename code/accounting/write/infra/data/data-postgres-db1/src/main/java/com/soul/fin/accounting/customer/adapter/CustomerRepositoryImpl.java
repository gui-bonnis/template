package com.soul.fin.accounting.customer.adapter;

import com.soul.fin.accounting.customer.ports.output.repository.CustomerRepository;
import com.soul.fin.accounting.customer.entity.Customer;
import com.soul.fin.accounting.customer.mapper.CustomerMapper;
import com.soul.fin.accounting.customer.repository.CustomerJpaRepository;
import org.springframework.data.domain.PageRequest;
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
    public Flux<Customer> findBy(int page, int size) {
        return customerJpaRepository.findBy(PageRequest.of(page - 1, size))
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

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return customerJpaRepository.existsById(id);
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return customerJpaRepository.deleteById(id);
    }
}
