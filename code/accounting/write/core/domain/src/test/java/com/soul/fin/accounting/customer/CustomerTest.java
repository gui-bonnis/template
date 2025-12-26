package com.soul.fin.accounting.customer;

import com.soul.fin.accounting.customer.entity.Customer;
import com.soul.fin.accounting.customer.vo.CustomerId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerTest {

    @Test
    void testCustomerCreation() {
        Customer customer = Customer.builder()
                .withCustomerId(new CustomerId(UUID.randomUUID()))
                .withName("John Doe")
                .build();
        assertEquals("John Doe", customer.getName());
    }

}