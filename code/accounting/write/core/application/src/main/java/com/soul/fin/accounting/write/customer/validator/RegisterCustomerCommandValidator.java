package com.soul.fin.accounting.write.customer.validator;

import com.soul.fin.accounting.write.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.accounting.write.customer.exception.CustomerApplicationExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RegisterCustomerCommandValidator {

    public static UnaryOperator<Mono<RegisterCustomerCommand>> validate() {
        return mono -> mono
                .filter(hasName())
                .switchIfEmpty(CustomerApplicationExceptions.missingName())
//                .filter(hasValidEmail())
//                .switchIfEmpty(CustomerApplicationExceptions.missingValidEmail())
                ;
    }

    private static Predicate<RegisterCustomerCommand> hasName() {
        return dto -> Objects.nonNull(dto.name());
    }

//    private static Predicate<CustomerDto> hasValidEmail() {
//        return dto -> Objects.nonNull(dto.email()) && dto.email().contains("@");
//    }

}
