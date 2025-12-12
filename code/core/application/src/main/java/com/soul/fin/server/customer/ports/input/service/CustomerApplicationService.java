package com.soul.fin.server.customer.ports.input.service;

import com.soul.fin.server.customer.dto.command.CustomerRegisteredResponse;
import com.soul.fin.server.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.server.customer.dto.query.CustomerQuery;
import com.soul.fin.server.customer.dto.query.GetCustomerByIdQuery;
import reactor.core.publisher.Mono;

public interface CustomerApplicationService {

    Mono<CustomerRegisteredResponse> registerCustomer(Mono<RegisterCustomerCommand> command);
    Mono<CustomerQuery> getCustomerById(Mono<GetCustomerByIdQuery> query);

}
