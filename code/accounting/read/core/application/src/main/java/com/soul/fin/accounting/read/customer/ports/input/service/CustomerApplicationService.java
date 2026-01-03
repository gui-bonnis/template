package com.soul.fin.accounting.read.customer.ports.input.service;

import com.soul.fin.accounting.read.customer.dto.query.CustomerQuery;
import com.soul.fin.accounting.read.customer.dto.query.GetAllCustomersPaginatedQuery;
import com.soul.fin.accounting.read.customer.dto.query.GetAllCustomersQuery;
import com.soul.fin.accounting.read.customer.dto.query.GetCustomerByIdQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerApplicationService {

    Mono<CustomerQuery> getCustomerById(Mono<GetCustomerByIdQuery> query);

    Flux<CustomerQuery> getAllCustomers(Flux<GetAllCustomersQuery> query);

    Flux<CustomerQuery> getAllCustomersPaginated(Flux<GetAllCustomersPaginatedQuery> query);


}
