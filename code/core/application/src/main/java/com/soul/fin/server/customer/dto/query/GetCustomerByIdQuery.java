package com.soul.fin.server.customer.dto.query;

import com.soul.fin.common.bus.core.Query;

import java.util.UUID;

public record GetCustomerByIdQuery(UUID customerId) implements Query<CustomerQuery> {
}