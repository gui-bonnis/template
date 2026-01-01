package com.soul.fin.accounting.customer.dto.command;

import com.soul.fin.common.bus.core.Command;
import com.soul.fin.common.bus.core.CommandMetadata;

import java.util.UUID;

public record DeleteCustomerCommand(CommandMetadata metadata,
                                    UUID customerId) implements Command<Void> {
}
