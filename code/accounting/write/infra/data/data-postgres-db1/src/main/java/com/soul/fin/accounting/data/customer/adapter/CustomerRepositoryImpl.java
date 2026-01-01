package com.soul.fin.accounting.data.customer.adapter;

import com.soul.fin.accounting.customer.entity.Customer;
import com.soul.fin.accounting.customer.ports.output.repository.CustomerRepository;
import com.soul.fin.accounting.data.customer.entity.CustomerEntity;
import com.soul.fin.accounting.data.customer.mapper.CustomerMapper;
import com.soul.fin.accounting.data.customer.repository.CustomerReactiveRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerReactiveRepository customerReactiveRepository;
    private final CustomerMapper mapper;

    public CustomerRepositoryImpl(CustomerReactiveRepository customerReactiveRepository, CustomerMapper mapper) {
        this.customerReactiveRepository = customerReactiveRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Customer> findById(UUID id) {
        return customerReactiveRepository.findById(id)
                .map(mapper::toCustomer);
    }

    @Override
    public Flux<Customer> findBy(int page, int size) {
        return customerReactiveRepository.findBy(PageRequest.of(page - 1, size))
                .map(mapper::toCustomer);
    }

    @Override
    public Flux<Customer> findAll() {
        return customerReactiveRepository.findAll()
                .map(mapper::toCustomer);
    }

    @Override
    public Mono<Customer> save(Customer customer) {
        return Mono.just(customer)
                .map(mapper::toCustomerEntity)
                .flatMap(customerReactiveRepository::save)
                .map(mapper::toCustomer);
    }

    @Override
    public Mono<Customer> insert(Customer customer) {
        return Mono.just(customer)
                .map(mapper::toCustomerEntity)
                .map(CustomerEntity::insert)
                .flatMap(customerReactiveRepository::save)
                .map(mapper::toCustomer);
    }

    @Override
    public Mono<Customer> update(Customer customer) {
        return Mono.just(customer)
                .map(mapper::toCustomerEntity)
                .map(CustomerEntity::update)
                .flatMap(customerReactiveRepository::save)
                .map(mapper::toCustomer);
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return customerReactiveRepository.existsById(id);
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return customerReactiveRepository.deleteById(id);
    }
}
