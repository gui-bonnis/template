package com.soul.fin.server.customer.service;

import com.soul.fin.common.bus.SpringCommandBus;
import com.soul.fin.common.bus.SpringQueryBus;
import com.soul.fin.server.customer.dto.command.CustomerRegisteredResponse;
import com.soul.fin.server.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.server.customer.dto.query.CustomerQuery;
import com.soul.fin.server.customer.dto.query.GetCustomerByIdQuery;
import com.soul.fin.server.customer.mapper.CustomerMapper;
import com.soul.fin.server.customer.ports.input.service.CustomerApplicationService;
import com.soul.fin.server.customer.ports.output.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerApplicationServiceImpl implements CustomerApplicationService {

    private final SpringCommandBus commandBus;
    private final SpringQueryBus queryBus;
    private final CustomerRepository customerRepository;

    public CustomerApplicationServiceImpl(SpringCommandBus commandBus, SpringQueryBus queryBus, CustomerRepository customerRepository) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
        this.customerRepository = customerRepository;
    }

    @Override
    public Mono<CustomerQuery> getCustomerById(Mono<GetCustomerByIdQuery> query) {

        return query.flatMap(queryBus::execute);

    }

    @Override
    public Flux<CustomerQuery> getAllCustomers() {

        return customerRepository.findAll()
                .map(CustomerMapper::toQuery);

    }

    @Override
    public Mono<CustomerRegisteredResponse> registerCustomer(Mono<RegisterCustomerCommand> command) {

        return command.flatMap(commandBus::execute);

    }

}
