package com.soul.fin.server.customer.dto.command;

import com.soul.fin.common.bus.core.Command;

import java.util.UUID;

public record RegisterCustomerCommand(UUID customerId) implements Command<CustomerRegisteredResponse> {
}
