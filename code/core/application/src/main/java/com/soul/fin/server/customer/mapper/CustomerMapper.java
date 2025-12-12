package com.soul.fin.server.customer.mapper;

import com.soul.fin.server.customer.dto.command.CustomerRegisteredResponse;
import com.soul.fin.server.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.server.customer.dto.query.CustomerQuery;
import com.soul.fin.service.customer.entity.Customer;
import reactor.core.publisher.Mono;


public class CustomerMapper {

    public static Mono<Customer> toCustomer(RegisterCustomerCommand command) {
        return Mono.just(command)
                .map(cmd -> Customer.builder()
                        .withName(cmd.name())
                        .build());
    }


    public static CustomerRegisteredResponse toResponse(final Customer customer) {
        return new CustomerRegisteredResponse(customer.getId().value());
    }

    public static CustomerQuery toQuery(final Customer customer) {
        return new CustomerQuery(
                customer.getId().value(),
                customer.getName()
        );
    }
}
