package com.soul.fin.accounting.customer.service;

import com.soul.fin.common.bus.SpringCommandBus;
import com.soul.fin.common.bus.SpringQueryBus;
import com.soul.fin.accounting.customer.dto.command.*;
import com.soul.fin.accounting.customer.dto.query.CustomerQuery;
import com.soul.fin.accounting.customer.dto.query.GetAllCustomersPaginatedQuery;
import com.soul.fin.accounting.customer.dto.query.GetAllCustomersQuery;
import com.soul.fin.accounting.customer.dto.query.GetCustomerByIdQuery;
import com.soul.fin.accounting.customer.ports.input.service.CustomerApplicationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerApplicationServiceImpl implements CustomerApplicationService {

    private final SpringCommandBus commandBus;
    private final SpringQueryBus queryBus;

    public CustomerApplicationServiceImpl(SpringCommandBus commandBus, SpringQueryBus queryBus) {
        this.commandBus = commandBus;
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
    public Mono<CustomerRegisteredResponse> registerCustomer(Mono<RegisterCustomerCommand> command) {
        return command.flatMap(commandBus::execute);
    }

    @Override
    public Mono<CustomerUpdatedResponse> updateCustomer(Mono<UpdateCustomerCommand> command) {
        return command.flatMap(commandBus::execute);
    }

    @Override
    public Mono<Void> deleteCustomer(Mono<DeleteCustomerCommand> command) {
        return command.flatMap(commandBus::execute);
    }

}
