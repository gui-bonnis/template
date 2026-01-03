package com.soul.fin.accounting.read.mapper;

import com.soul.fin.accounting.read.customer.dto.query.CustomerQuery;
import com.soul.fin.accounting.read.customer.dto.query.GetCustomerByIdQuery;
import com.soul.fin.accounting.read.dto.request.GetCustomerRequest;
import com.soul.fin.accounting.read.dto.response.CustomerQueryResponse;

public class CustomerMapper {


    public static GetCustomerByIdQuery toQuery(GetCustomerRequest request) {
        return new GetCustomerByIdQuery(request.customerId());
    }

    public static CustomerQueryResponse toQueryResponse(CustomerQuery query) {
        return new CustomerQueryResponse(query.customerId(), query.name());
    }


}
