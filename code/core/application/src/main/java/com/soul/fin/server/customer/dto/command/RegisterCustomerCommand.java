package com.soul.fin.server.customer.dto.command;

import com.soul.fin.common.bus.core.Command;

public record RegisterCustomerCommand(String name) implements Command<CustomerRegisteredResponse> {
}
