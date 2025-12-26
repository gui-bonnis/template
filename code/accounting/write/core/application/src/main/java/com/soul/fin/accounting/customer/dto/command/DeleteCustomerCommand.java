package com.soul.fin.accounting.customer.dto.command;

import com.soul.fin.common.bus.core.Command;

import java.util.UUID;

public record DeleteCustomerCommand(UUID customerId) implements Command<Void> {
}
