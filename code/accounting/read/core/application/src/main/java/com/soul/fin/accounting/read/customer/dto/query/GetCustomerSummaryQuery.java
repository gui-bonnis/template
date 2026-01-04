package com.soul.fin.accounting.read.customer.dto.query;

import com.soul.fin.common.bus.core.query.QueryOne;

import java.util.UUID;

public record GetCustomerSummaryQuery(UUID customerId) implements QueryOne<CustomerQuery> {
}