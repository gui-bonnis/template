package com.soul.fin.accounting.read.customer.service;

import com.soul.fin.accounting.read.customer.entity.Customer;

public interface CustomerDomainService {

    Customer registerCustomer(Customer customer);

    Customer updateCustomer(Customer fromDb, Customer fromCmd);

    Customer deleteCustomer(Customer customer);

}
