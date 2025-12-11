package com.soul.fin.service.customer.service;

import com.soul.fin.service.customer.entity.Customer;
import com.soul.fin.service.customer.vo.CustomerId;

public interface CustomerDomainService {

    public Customer registerCustomer(CustomerId customerId);
}
