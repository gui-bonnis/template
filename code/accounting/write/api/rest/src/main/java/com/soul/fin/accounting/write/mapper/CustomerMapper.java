package com.soul.fin.accounting.write.mapper;

import com.soul.fin.accounting.write.customer.dto.command.CustomerUpdatedResponse;
import com.soul.fin.accounting.write.customer.dto.command.DeleteCustomerCommand;
import com.soul.fin.accounting.write.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.accounting.write.customer.dto.command.UpdateCustomerCommand;
import com.soul.fin.accounting.write.dto.request.DeleteCustomerRequest;
import com.soul.fin.accounting.write.dto.request.RegisterCustomerRequest;
import com.soul.fin.accounting.write.dto.request.UpdateCustomerRequest;
import com.soul.fin.accounting.write.dto.response.CustomerRegisteredResponse;
import com.soul.fin.common.bus.core.command.CommandMetadata;

import java.util.UUID;

public class CustomerMapper {

    public static RegisterCustomerCommand toCommand(RegisterCustomerRequest request) {
        CommandMetadata metadata = new CommandMetadata();
        metadata.setCommandId(UUID.randomUUID());
        return new RegisterCustomerCommand(metadata, request.name());
    }

    public static UpdateCustomerCommand toCommand(UUID customerId, UpdateCustomerRequest request) {
        CommandMetadata metadata = new CommandMetadata();
        metadata.setCommandId(UUID.randomUUID());
        return new UpdateCustomerCommand(metadata, customerId, request.name());
    }

    public static DeleteCustomerCommand toCommand(DeleteCustomerRequest request) {
        CommandMetadata metadata = new CommandMetadata();
        metadata.setCommandId(UUID.randomUUID());
        return new DeleteCustomerCommand(metadata, request.customerId());
    }

    public static CustomerRegisteredResponse toResponse(com.soul.fin.accounting.write.customer.dto.command.CustomerRegisteredResponse response) {
        return new CustomerRegisteredResponse(response.customerId());
    }

    public static CustomerUpdatedResponse toResponse(CustomerUpdatedResponse response) {
        return new CustomerUpdatedResponse(response.customerId());
    }

}
