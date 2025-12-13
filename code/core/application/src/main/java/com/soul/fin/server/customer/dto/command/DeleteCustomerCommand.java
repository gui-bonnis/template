package com.soul.fin.server.customer.dto.command;

import com.soul.fin.common.bus.core.Command;

import java.util.UUID;

public record DeleteCustomerCommand(UUID customerId) implements Command<Void> {
}
