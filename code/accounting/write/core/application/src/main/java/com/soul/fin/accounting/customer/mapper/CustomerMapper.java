package com.soul.fin.accounting.customer.mapper;

import com.soul.fin.accounting.customer.dto.command.*;
import com.soul.fin.accounting.customer.entity.Customer;
import com.soul.fin.accounting.customer.vo.CustomerId;


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
        return new CustomerRegisteredResponse(customer.getId().getValue());
    }

    public static CustomerUpdatedResponse toCustomerUpdatedResponse(final Customer customer) {
        return new CustomerUpdatedResponse(customer.getId().getValue());
    }

}
