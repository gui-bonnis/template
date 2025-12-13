package com.soul.fin.service.customer.exception;

import com.soul.fin.common.core.exception.ApplicationExceptions;
import com.soul.fin.common.core.exception.InvalidInputException;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class CustomerDomainExceptions extends ApplicationExceptions {

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
