package com.soul.fin.accounting.write.customer.handler.command;


import com.soul.fin.accounting.write.customer.dto.command.CustomerRegisteredResponse;
import com.soul.fin.accounting.write.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.accounting.write.customer.usecase.RegisterCustomerUseCase;
import com.soul.fin.common.bus.core.CommandHandler;
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