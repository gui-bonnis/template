package com.soul.fin.server.customer.validator;

import com.soul.fin.server.customer.dto.command.UpdateCustomerCommand;
import com.soul.fin.server.customer.exception.CustomerApplicationExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class UpdateCustomerCommandValidator {

    public static UnaryOperator<Mono<UpdateCustomerCommand>> validate() {
        return mono -> mono
                .filter(hasName())
                .switchIfEmpty(CustomerApplicationExceptions.missingName())
//                .filter(hasValidEmail())
//                .switchIfEmpty(CustomerApplicationExceptions.missingValidEmail())
                ;
    }

    private static Predicate<UpdateCustomerCommand> hasName() {
        return dto -> Objects.nonNull(dto.name());
    }

//    private static Predicate<CustomerDto> hasValidEmail() {
//        return dto -> Objects.nonNull(dto.email()) && dto.email().contains("@");
//    }

}
