package com.soul.fin.service.mapper;

import com.soul.fin.server.customer.dto.command.CustomerUpdatedResponse;
import com.soul.fin.server.customer.dto.command.DeleteCustomerCommand;
import com.soul.fin.server.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.server.customer.dto.command.UpdateCustomerCommand;
import com.soul.fin.server.customer.dto.query.GetCustomerByIdQuery;
import com.soul.fin.service.dto.request.DeleteCustomerRequest;
import com.soul.fin.service.dto.request.GetCustomerRequest;
import com.soul.fin.service.dto.request.RegisterCustomerRequest;
import com.soul.fin.service.dto.request.UpdateCustomerRequest;
import com.soul.fin.service.dto.response.CustomerQueryResponse;
import com.soul.fin.service.dto.response.CustomerRegisteredResponse;

import java.util.UUID;

public class CustomerMapper {

    public static RegisterCustomerCommand toCommand(RegisterCustomerRequest request) {
        return new RegisterCustomerCommand(request.name());
    }

    public static UpdateCustomerCommand toCommand(UUID customerId, UpdateCustomerRequest request) {
        return new UpdateCustomerCommand(customerId, request.name());
    }

    public static DeleteCustomerCommand toCommand(DeleteCustomerRequest request) {
        return new DeleteCustomerCommand(request.customerId());
    }

    public static CustomerRegisteredResponse toResponse(com.soul.fin.server.customer.dto.command.CustomerRegisteredResponse response) {
        return new CustomerRegisteredResponse(response.customerId());
    }

    public static CustomerUpdatedResponse toResponse(com.soul.fin.server.customer.dto.command.CustomerUpdatedResponse response) {
        return new CustomerUpdatedResponse(response.customerId());
    }

    public static GetCustomerByIdQuery toQuery(GetCustomerRequest request) {
        return new GetCustomerByIdQuery(request.customerId());
    }

    public static CustomerQueryResponse toQueryResponse(com.soul.fin.server.customer.dto.query.CustomerQuery query) {
        return new CustomerQueryResponse(query.customerId(), query.name());
    }


}
