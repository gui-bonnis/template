package com.soul.fin.accounting.customer.mapper;

import com.soul.fin.accounting.customer.entity.Customer;
import com.soul.fin.accounting.customer.entity.CustomerEntity;
import com.soul.fin.accounting.customer.vo.CustomerId;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toCustomer(CustomerEntity customerEntity) {
        return Customer.builder()
                .withCustomerId(new CustomerId(customerEntity.getId()))
                .withName(customerEntity.getName())
                .build();
    }

    public CustomerEntity toCustomerEntity(Customer customer) {
//        return CustomerEntity.builder()
//                .id(customer.getId().value())
//                .name(customer.getName())
//                .build();
        final var customerId = customer.getId() != null ? customer.getId().value() : null;
        return new CustomerEntity(customerId, customer.getName());
    }

}
