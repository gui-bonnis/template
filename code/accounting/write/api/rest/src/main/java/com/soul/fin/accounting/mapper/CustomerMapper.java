package com.soul.fin.accounting.mapper;

import com.soul.fin.accounting.customer.dto.command.CustomerUpdatedResponse;
import com.soul.fin.accounting.customer.dto.command.DeleteCustomerCommand;
import com.soul.fin.accounting.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.accounting.customer.dto.command.UpdateCustomerCommand;
import com.soul.fin.accounting.customer.dto.query.GetCustomerByIdQuery;
import com.soul.fin.accounting.dto.request.DeleteCustomerRequest;
import com.soul.fin.accounting.dto.request.GetCustomerRequest;
import com.soul.fin.accounting.dto.request.RegisterCustomerRequest;
import com.soul.fin.accounting.dto.request.UpdateCustomerRequest;
import com.soul.fin.accounting.dto.response.CustomerQueryResponse;
import com.soul.fin.accounting.dto.response.CustomerRegisteredResponse;

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

    public static CustomerRegisteredResponse toResponse(com.soul.fin.accounting.customer.dto.command.CustomerRegisteredResponse response) {
        return new CustomerRegisteredResponse(response.customerId());
    }

    public static CustomerUpdatedResponse toResponse(com.soul.fin.accounting.customer.dto.command.CustomerUpdatedResponse response) {
        return new CustomerUpdatedResponse(response.customerId());
    }

    public static GetCustomerByIdQuery toQuery(GetCustomerRequest request) {
        return new GetCustomerByIdQuery(request.customerId());
    }

    public static CustomerQueryResponse toQueryResponse(com.soul.fin.accounting.customer.dto.query.CustomerQuery query) {
        return new CustomerQueryResponse(query.customerId(), query.name());
    }


}
