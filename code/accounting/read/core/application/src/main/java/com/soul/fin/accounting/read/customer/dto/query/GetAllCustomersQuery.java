package com.soul.fin.accounting.read.customer.dto.query;

import com.soul.fin.common.bus.core.query.QueryMany;

public record GetAllCustomersQuery() implements QueryMany<CustomerQuery> {
}