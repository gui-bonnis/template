package com.soul.fin.accounting.read.data.customer.adapter;

import com.soul.fin.accounting.read.customer.entity.Customer;
import com.soul.fin.accounting.read.customer.ports.output.repository.CustomerRepository;
import com.soul.fin.accounting.read.data.customer.mapper.CustomerMapper;
import com.soul.fin.accounting.read.data.customer.repository.CustomerReactiveRepository;
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
    public Mono<Boolean> existsById(UUID id) {
        return customerReactiveRepository.existsById(id);
    }

}
