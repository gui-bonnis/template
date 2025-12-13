package com.soul.fin.server.customer.dto.query;

import com.soul.fin.common.bus.core.QueryMany;

public record GetAllCustomersPaginatedQuery(int page,
                                            int size) implements QueryMany<CustomerQuery> {
}