package com.soul.fin.server.customer.dto.command;

import com.soul.fin.common.bus.core.Command;

import java.util.UUID;

public record UpdateCustomerCommand(UUID customerId, String name) implements Command<CustomerUpdatedResponse> {
}
