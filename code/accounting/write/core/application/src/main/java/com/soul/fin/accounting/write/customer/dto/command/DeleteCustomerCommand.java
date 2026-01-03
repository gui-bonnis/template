package com.soul.fin.accounting.write.customer.dto.command;

import com.soul.fin.common.bus.core.command.Command;
import com.soul.fin.common.bus.core.command.CommandMetadata;

import java.util.UUID;

public record DeleteCustomerCommand(CommandMetadata metadata,
                                    UUID customerId) implements Command<Void> {
}
