package com.soul.fin.accounting.write.customer.exception;

import com.soul.fin.common.core.exception.ApplicationExceptions;
import com.soul.fin.common.core.exception.EntityNotFoundException;
import com.soul.fin.common.core.exception.InvalidInputException;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class CustomerApplicationExceptions extends ApplicationExceptions {

    public static <T> Mono<T> customerNotFound(UUID customerId) {
        final String msg = "Customer not found with ID: %s";
        return Mono.error(new EntityNotFoundException(msg.formatted(customerId)));
    }

    public static <T> Mono<T> missingId() {
        final String msg = "Customer ID is missing";
        return Mono.error(new EntityNotFoundException(msg));
    }

    public static <T> Mono<T> missingName(UUID customerId) {
        final String msg = "Missing name for customer with ID: %s";
        return Mono.error(new InvalidInputException(msg.formatted(customerId)));
    }

    public static <T> Mono<T> missingName() {
        final String msg = "Missing name for customer";
        return Mono.error(new InvalidInputException(msg));
    }

    public static <T> Mono<T> missingValidEmail(UUID customerId) {
        final String msg = "Missing valid email for customer with ID: %s";
        return Mono.error(new InvalidInputException(msg.formatted(customerId)));
    }

}
