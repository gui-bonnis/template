package com.soul.fin.server.customer.dto.query;

import com.soul.fin.common.bus.core.Command;
import com.soul.fin.common.bus.core.Query;
import com.soul.fin.server.customer.dto.command.CustomerRegisteredResponse;

import java.util.UUID;

public record GetCustomerByIdQuery(UUID customerId) implements Query<CustomerQuery> {
}