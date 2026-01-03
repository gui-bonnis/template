package com.soul.fin.accounting.write.customer.service;

import com.soul.fin.accounting.write.customer.entity.Customer;

public interface CustomerDomainService {

    Customer registerCustomer(Customer customer);

    Customer updateCustomer(Customer fromDb, Customer fromCmd);

    Customer deleteCustomer(Customer customer);

}
