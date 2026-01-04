package com.soul.fin.accounting.read.data.customer.adapter;

import com.soul.fin.accounting.read.customer.entity.CustomerSummary;
import com.soul.fin.accounting.read.customer.ports.output.repository.CustomerSummaryRepository;
import com.soul.fin.accounting.read.data.customer.mapper.CustomerSummaryMapper;
import com.soul.fin.accounting.read.data.customer.repository.CustomerSummaryReactiveRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CustomerSummaryRepositoryImpl implements CustomerSummaryRepository {

    private final CustomerSummaryReactiveRepository repository;
    private final CustomerSummaryMapper mapper;

    public CustomerSummaryRepositoryImpl(CustomerSummaryReactiveRepository repository, CustomerSummaryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<CustomerSummary> findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toCustomerSummary);
    }

    @Override
    public Flux<CustomerSummary> findBy(int page, int size) {
        return repository.findBy(PageRequest.of(page - 1, size))
                .map(mapper::toCustomerSummary);
    }

    @Override
    public Flux<CustomerSummary> findAll() {
        return repository.findAll()
                .map(mapper::toCustomerSummary);
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return repository.existsById(id);
    }

}
