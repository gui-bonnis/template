package com.soul.fin.accounting.write.customer.dto.command;

import com.soul.fin.common.bus.core.Command;
import com.soul.fin.common.bus.core.CommandMetadata;

public record RegisterCustomerCommand(CommandMetadata metadata,
                                      String name) implements Command<CustomerRegisteredResponse> {
}
