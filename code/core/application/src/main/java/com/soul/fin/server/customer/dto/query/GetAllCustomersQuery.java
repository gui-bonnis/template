package com.soul.fin.server.customer.dto.query;

import com.soul.fin.common.bus.core.QueryMany;

public record GetAllCustomersQuery() implements QueryMany<CustomerQuery> {
}