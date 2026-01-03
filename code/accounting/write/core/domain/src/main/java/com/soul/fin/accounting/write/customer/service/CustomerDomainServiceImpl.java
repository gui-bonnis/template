package com.soul.fin.accounting.write.customer.service;

import com.soul.fin.accounting.write.customer.entity.Customer;
import com.soul.fin.accounting.write.customer.event.CustomerRegisteredEvent;
import com.soul.fin.accounting.write.customer.event.CustomerValidatedEvent;
import com.soul.fin.accounting.write.customer.vo.CustomerId;

import java.time.Instant;
import java.util.UUID;


public class CustomerDomainServiceImpl implements CustomerDomainService {

    //private static final Logger log = LoggerFactory.getLogger(CustomerDomainServiceImpl.class);

    public Customer registerCustomer(Customer customer) {
        var cust = Customer.builder()
                .withCustomerId(new CustomerId(UUID.randomUUID()))
                .withName(customer.getName())
                .build();

        cust.registerEvent(new CustomerRegisteredEvent(cust.getId().getValue(), customer.getAggregateVersion(), UUID.randomUUID(), Instant.now()));

        return cust;
    }

    @Override
    public Customer updateCustomer(Customer fromDb, Customer fromCmd) {

        var cust = Customer.builder()
                .withCustomerId(fromDb.getId())
                .withName(fromCmd.getName())
                .build();

        cust.registerEvent(new CustomerValidatedEvent(cust.getId().getValue(), fromDb.getAggregateVersion(), UUID.randomUUID(), Instant.now()));

        return cust;
    }

    @Override
    public Customer deleteCustomer(Customer customer) {
        return customer;
    }
}