package com.soul.fin.service.customer.service;

import com.soul.fin.service.customer.entity.Customer;
import com.soul.fin.service.customer.vo.CustomerId;


public class CustomerDomainServiceImpl implements CustomerDomainService {
    @Override
    public Customer registerCustomer(CustomerId customerId) {
        return Customer.builder()
                .withCustomerId(customerId)
                .build();
    }
}
