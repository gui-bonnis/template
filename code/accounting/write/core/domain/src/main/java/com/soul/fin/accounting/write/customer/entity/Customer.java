package com.soul.fin.accounting.write.customer.entity;

import com.soul.fin.accounting.write.customer.event.CustomerRegisteredEvent;
import com.soul.fin.accounting.write.customer.vo.CustomerId;
import com.soul.fin.common.core.entity.BaseAggregateRoot;
import com.soul.fin.common.core.event.DomainEvent;

public class Customer extends BaseAggregateRoot<CustomerId> {
    static final Integer CURRENT_SHEMA_VERSION = 1;
    //private final String name;
    private String name;

    public String getName() {
        return name;
    }

    private Customer(Builder builder) {
        super.setId(builder.customerId);
        super.setAggregateVersion(builder.aggregateVersion);
        name = builder.name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private CustomerId customerId;
        private long aggregateVersion;
        private String name;

        private Builder() {
        }

        public Builder withCustomerId(CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder withAggregateVersion(long aggregateVersion) {
            this.aggregateVersion = aggregateVersion;
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

    public void when(DomainEvent event) {
        switch (event) {
            case CustomerRegisteredEvent e -> {
                this.setId(new CustomerId(e.aggregateId()));
                this.name = e.aggregateId().toString();
                this.setAggregateVersion(e.aggregateVersion());
            }
//            case CustomerRenamed e -> {
//                this.name = e.newName();
//                this.version = e.version();
//            }
            default -> throw new IllegalStateException("Unexpected value: " + event);
        }
    }
}
