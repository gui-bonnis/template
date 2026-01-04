package com.soul.fin.accounting.write.data.customer.mapper;

import com.soul.fin.accounting.write.customer.entity.Customer;
import com.soul.fin.accounting.write.customer.vo.CustomerId;
import com.soul.fin.accounting.write.data.customer.entity.CustomerEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toCustomer(CustomerEntity customerEntity) {
        return Customer.builder()
                .withCustomerId(new CustomerId(customerEntity.getId()))
                .withAggregateVersion(customerEntity.getVersion())
                .withName(customerEntity.getName())
                .build();
    }

    public CustomerEntity toCustomerEntity(Customer customer) {
        final var customerId = customer.getId() != null ? customer.getId().getValue() : null;
        return new CustomerEntity(customerId, customer.getAggregateVersion(), customer.getName());
    }


}
