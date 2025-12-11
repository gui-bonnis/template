package com.soul.fin.service.customer;

import com.soul.fin.service.customer.entity.Customer;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerTest {

    @Test
    void testCustomerCreation() {
        Customer customer = Customer.builder()
                .withCustomerId(new com.soul.fin.service.customer.vo.CustomerId(UUID.randomUUID()))
                .withName("John Doe")
                .build();
        assertEquals("John Doe", customer.getName());
    }

}