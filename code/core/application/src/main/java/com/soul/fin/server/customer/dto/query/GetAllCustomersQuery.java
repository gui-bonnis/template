package com.soul.fin.server.customer.dto.query;

import com.soul.fin.common.bus.core.Query;

public record GetAllCustomersQuery() implements Query<CustomersQuery> {
}