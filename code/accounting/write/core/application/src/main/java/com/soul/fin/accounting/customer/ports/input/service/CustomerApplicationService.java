package com.soul.fin.accounting.customer.ports.input.service;

import com.soul.fin.accounting.customer.dto.command.*;
import com.soul.fin.accounting.customer.dto.query.CustomerQuery;
import com.soul.fin.accounting.customer.dto.query.GetAllCustomersPaginatedQuery;
import com.soul.fin.accounting.customer.dto.query.GetAllCustomersQuery;
import com.soul.fin.accounting.customer.dto.query.GetCustomerByIdQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerApplicationService {

    Mono<CustomerQuery> getCustomerById(Mono<GetCustomerByIdQuery> query);

    Flux<CustomerQuery> getAllCustomers(Flux<GetAllCustomersQuery> query);

    Flux<CustomerQuery> getAllCustomersPaginated(Flux<GetAllCustomersPaginatedQuery> query);

    Mono<CustomerRegisteredResponse> registerCustomer(Mono<RegisterCustomerCommand> command);

    Mono<CustomerUpdatedResponse> updateCustomer(Mono<UpdateCustomerCommand> command);

    Mono<Void> deleteCustomer(Mono<DeleteCustomerCommand> command);


}
