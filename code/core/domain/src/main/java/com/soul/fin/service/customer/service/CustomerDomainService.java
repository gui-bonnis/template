package com.soul.fin.service.customer.service;

import com.soul.fin.service.customer.entity.Customer;

public interface CustomerDomainService {

    Customer registerCustomer(Customer customer);

    Customer updateCustomer(Customer fromDb, Customer fromCmd);

    Customer deleteCustomer(Customer customer);

}
