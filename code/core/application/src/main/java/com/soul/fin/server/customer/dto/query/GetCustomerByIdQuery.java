package com.soul.fin.server.customer.dto.query;

import com.soul.fin.common.bus.core.QueryOne;

import java.util.UUID;

public record GetCustomerByIdQuery(UUID customerId) implements QueryOne<CustomerQuery> {
}