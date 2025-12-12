package com.soul.fin.service.mapper;

import com.soul.fin.server.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.server.customer.dto.query.GetCustomerByIdQuery;
import com.soul.fin.service.dto.CustomerQueryResponse;
import com.soul.fin.service.dto.CustomerRegisteredResponse;
import com.soul.fin.service.dto.GetCustomerRequest;
import com.soul.fin.service.dto.RegisterCustomerRequest;

public class CustomerMapper {

    public static RegisterCustomerCommand toCommand(RegisterCustomerRequest request) {
        return new RegisterCustomerCommand(request.name());
    }

    public static CustomerRegisteredResponse toResponse(com.soul.fin.server.customer.dto.command.CustomerRegisteredResponse response) {
        return new CustomerRegisteredResponse(response.customerId());
    }

    public static GetCustomerByIdQuery toQuery(GetCustomerRequest request) {
        return new GetCustomerByIdQuery(request.customerId());
    }

    public static CustomerQueryResponse toQueryResponse(com.soul.fin.server.customer.dto.query.CustomerQuery query) {
        return new CustomerQueryResponse(query.customerId(), query.name());
    }


}
