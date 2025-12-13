package com.soul.fin.server.customer.ports.input.service;

import com.soul.fin.server.customer.dto.command.*;
import com.soul.fin.server.customer.dto.query.CustomerQuery;
import com.soul.fin.server.customer.dto.query.GetCustomerByIdQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerApplicationService {

    Mono<CustomerQuery> getCustomerById(Mono<GetCustomerByIdQuery> query);

    Flux<CustomerQuery> getAllCustomers();

    Mono<CustomerRegisteredResponse> registerCustomer(Mono<RegisterCustomerCommand> command);

    Mono<CustomerUpdatedResponse> updateCustomer(Mono<UpdateCustomerCommand> command);

    Mono<Void> deleteCustomer(Mono<DeleteCustomerCommand> command);


}
