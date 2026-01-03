package com.soul.fin.accounting.read.customer.dto.query;

import com.soul.fin.common.bus.core.query.QueryOne;

import java.util.UUID;

public record GetCustomerByIdQuery(UUID customerId) implements QueryOne<CustomerQuery> {
}