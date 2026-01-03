package com.soul.fin.accounting.write.customer.dto.command;

import com.soul.fin.common.bus.core.command.Command;
import com.soul.fin.common.bus.core.command.CommandMetadata;

import java.util.UUID;

public record UpdateCustomerCommand(CommandMetadata metadata,
                                    UUID customerId,
                                    String name) implements Command<CustomerUpdatedResponse> {
}
