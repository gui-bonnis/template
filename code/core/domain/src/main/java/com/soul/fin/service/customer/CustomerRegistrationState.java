package com.soul.fin.service.customer;

public sealed interface CustomerRegistrationState permits
        CustomerRegistrationState.Registering,
        CustomerRegistrationState.Validating,
        CustomerRegistrationState.Validated,
        CustomerRegistrationState.Registered,
        CustomerRegistrationState.Failed {

    CustomerDOP customerDOP();

    default void validateCustomer(CustomerDOP customerDOP) {
        if (customerDOP == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
    }

    record Registering(CustomerDOP customerDOP) implements CustomerRegistrationState {

        public Registering {
            validateCustomer(customerDOP);
        }
    }

    record Validating(CustomerDOP customerDOP) implements CustomerRegistrationState {

        public Validating {
            validateCustomer(customerDOP);
        }
    }

    record Validated(CustomerDOP customerDOP) implements CustomerRegistrationState {

        public Validated {
            validateCustomer(customerDOP);
        }
    }

    record Registered(CustomerDOP customerDOP) implements CustomerRegistrationState {
        public Registered {
            validateCustomer(customerDOP);
        }
    }

    record Failed(CustomerDOP customerDOP,
                  String reason) implements CustomerRegistrationState {
        public Failed {
            validateCustomer(customerDOP);
            if (reason == null) {
                throw new IllegalArgumentException("Reason cannot be null");
            }
        }
    }
}
