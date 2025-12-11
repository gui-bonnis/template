package com.soul.fin.service.mapper;

import com.soul.fin.server.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.service.dto.CustomerRegisteredResponse;
import com.soul.fin.service.dto.RegisterCustomer;

public class CustomerMapper {

    public static RegisterCustomerCommand toCommand(RegisterCustomer request) {
        return new RegisterCustomerCommand(request.customerId());
    }

    public static CustomerRegisteredResponse toResponse(com.soul.fin.server.customer.dto.command.CustomerRegisteredResponse response) {
        return new CustomerRegisteredResponse(response.customerId());
    }

}
