package com.soul.fin.accounting.write.customer.dto.command;

import com.soul.fin.common.bus.core.command.Command;
import com.soul.fin.common.bus.core.command.CommandMetadata;

public record RegisterCustomerCommand(CommandMetadata metadata,
                                      String name) implements Command<CustomerRegisteredResponse> {
}
