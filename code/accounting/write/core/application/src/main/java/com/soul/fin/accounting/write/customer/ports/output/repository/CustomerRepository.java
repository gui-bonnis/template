package com.soul.fin.accounting.write.customer.ports.output.repository;

import com.soul.fin.accounting.write.customer.entity.Customer;
import com.soul.fin.common.application.ports.output.repository.ReadRepository;
import com.soul.fin.common.command.application.ports.output.repository.WriteRepository;

public interface CustomerRepository extends ReadRepository<Customer>, WriteRepository<Customer> {

}
