package com.soul.fin.service.customer.service;

import com.soul.fin.service.customer.entity.Customer;


public class CustomerDomainServiceImpl implements CustomerDomainService {
    @Override
    public Customer registerCustomer(Customer customer) {
        return Customer.builder()
                .withName(customer.getName())
                .build();
    }
}
