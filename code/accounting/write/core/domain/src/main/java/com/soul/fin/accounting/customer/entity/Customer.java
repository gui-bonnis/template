package com.soul.fin.accounting.customer.entity;

import com.soul.fin.common.core.entity.AggregateRoot;
import com.soul.fin.accounting.customer.vo.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {
    private final String name;


    public String getName() {
        return name;
    }

    private Customer(Builder builder) {
        super.setId(builder.customerId);
        name = builder.name;
        //failureMessages = builder.failureMessages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private CustomerId customerId;
        private String name;

        private Builder() {
        }

        public static Builder aCustomer() {
            return new Builder();
        }

        public Builder withCustomerId(CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
