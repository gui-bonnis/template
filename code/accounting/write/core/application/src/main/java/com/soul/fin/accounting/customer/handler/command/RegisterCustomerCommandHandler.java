package com.soul.fin.accounting.customer.handler.command;


import com.soul.fin.common.bus.core.CommandHandler;
import com.soul.fin.accounting.customer.dto.command.CustomerRegisteredResponse;
import com.soul.fin.accounting.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.accounting.customer.usecase.RegisterCustomerUseCase;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RegisterCustomerCommandHandler implements CommandHandler<RegisterCustomerCommand, CustomerRegisteredResponse> {

    private final RegisterCustomerUseCase registerCustomerUseCase;

    public RegisterCustomerCommandHandler(RegisterCustomerUseCase registerCustomerUseCase) {
        this.registerCustomerUseCase = registerCustomerUseCase;
    }

    @Override
    public Mono<CustomerRegisteredResponse> handle(RegisterCustomerCommand command) {
        return registerCustomerUseCase.registerCustomer(command);
    }
}