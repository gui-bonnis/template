package com.soul.fin.accounting.customer.validator;

import com.soul.fin.accounting.customer.dto.command.DeleteCustomerCommand;
import com.soul.fin.accounting.customer.exception.CustomerApplicationExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class DeleteCustomerCommandValidator {

    public static UnaryOperator<Mono<DeleteCustomerCommand>> validate() {
        return mono -> mono
                .filter(hasId())
                .switchIfEmpty(CustomerApplicationExceptions.missingId())
//                .filter(hasValidEmail())
//                .switchIfEmpty(CustomerApplicationExceptions.missingValidEmail())
                ;
    }

    private static Predicate<DeleteCustomerCommand> hasId() {
        return dto -> Objects.nonNull(dto.customerId());
    }


//    private static Predicate<CustomerDto> hasValidEmail() {
//        return dto -> Objects.nonNull(dto.email()) && dto.email().contains("@");
//    }

}
