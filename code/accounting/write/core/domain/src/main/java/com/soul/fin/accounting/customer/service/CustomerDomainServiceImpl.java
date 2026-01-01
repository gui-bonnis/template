package com.soul.fin.accounting.customer.service;

import com.soul.fin.accounting.customer.entity.Customer;


public class CustomerDomainServiceImpl implements CustomerDomainService {

    //private static final Logger log = LoggerFactory.getLogger(CustomerDomainServiceImpl.class);

    public Customer registerCustomer(Customer customer) {
        return Customer.builder()
                .withName(customer.getName())
                .build();
    }

    @Override
    public Customer updateCustomer(Customer fromDb, Customer fromCmd) {
        return Customer.builder()
                .withCustomerId(fromDb.getId())
                .withName(fromCmd.getName())
                .build();
    }

    @Override
    public Customer deleteCustomer(Customer customer) {
        return customer;
    }
}