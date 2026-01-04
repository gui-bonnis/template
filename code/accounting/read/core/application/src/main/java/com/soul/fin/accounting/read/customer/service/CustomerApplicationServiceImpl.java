package com.soul.fin.accounting.read.customer.service;

import com.soul.fin.accounting.read.customer.dto.query.*;
import com.soul.fin.accounting.read.customer.ports.input.service.CustomerApplicationService;
import com.soul.fin.common.bus.SpringQueryBus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerApplicationServiceImpl implements CustomerApplicationService {

    private final SpringQueryBus queryBus;

    public CustomerApplicationServiceImpl(SpringQueryBus queryBus) {
        this.queryBus = queryBus;
    }

    @Override
    public Mono<CustomerQuery> getCustomerById(Mono<GetCustomerByIdQuery> query) {
        return query.flatMap(queryBus::ask);
    }

    @Override
    public Flux<CustomerQuery> getAllCustomers(Flux<GetAllCustomersQuery> query) {
        return query.flatMap(queryBus::askMany);
    }

    @Override
    public Flux<CustomerQuery> getAllCustomersPaginated(Flux<GetAllCustomersPaginatedQuery> query) {
        return query.flatMap(queryBus::askMany);
    }

    @Override
    public Mono<CustomerQuery> getCustomerSummary(Mono<GetCustomerSummaryQuery> query) {
        return query.flatMap(queryBus::ask);
    }

}
