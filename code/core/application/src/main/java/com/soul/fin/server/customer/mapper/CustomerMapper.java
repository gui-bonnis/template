package com.soul.fin.server.customer.mapper;

import com.soul.fin.server.customer.dto.command.*;
import com.soul.fin.service.customer.entity.Customer;
import com.soul.fin.service.customer.vo.CustomerId;


public class CustomerMapper {

    public static Customer toCustomer(RegisterCustomerCommand command) {
        return Customer.builder()
                .withName(command.name())
                .build();
    }

    public static Customer toCustomer(UpdateCustomerCommand command) {
        return Customer.builder()
                .withCustomerId(new CustomerId(command.customerId()))
                .withName(command.name())
                .build();
    }

    public static Customer toCustomer(DeleteCustomerCommand command) {
        return Customer.builder()
                .withCustomerId(new CustomerId(command.customerId()))
                .build();
    }


    public static CustomerRegisteredResponse toCustomerRegisteredResponse(final Customer customer) {
        return new CustomerRegisteredResponse(customer.getId().value());
    }

    public static CustomerUpdatedResponse toCustomerUpdatedResponse(final Customer customer) {
        return new CustomerUpdatedResponse(customer.getId().value());
    }

}
