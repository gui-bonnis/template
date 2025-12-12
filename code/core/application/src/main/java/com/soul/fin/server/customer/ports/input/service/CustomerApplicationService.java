package com.soul.fin.server.customer.ports.input.service;

import com.soul.fin.server.customer.dto.command.CustomerRegisteredResponse;
import com.soul.fin.server.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.server.customer.dto.query.CustomerQuery;
import com.soul.fin.server.customer.dto.query.GetCustomerByIdQuery;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerApplicationService {

    Mono<CustomerQuery> getCustomerById(@Validated Mono<GetCustomerByIdQuery> query);

    Flux<CustomerQuery> getAllCustomers();

    Mono<CustomerRegisteredResponse> registerCustomer(@Validated Mono<RegisterCustomerCommand> command);


}
