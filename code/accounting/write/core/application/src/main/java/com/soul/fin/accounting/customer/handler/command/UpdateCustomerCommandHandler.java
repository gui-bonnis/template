package com.soul.fin.accounting.customer.handler.command;


import com.soul.fin.common.bus.core.CommandHandler;
import com.soul.fin.accounting.customer.dto.command.CustomerUpdatedResponse;
import com.soul.fin.accounting.customer.dto.command.UpdateCustomerCommand;
import com.soul.fin.accounting.customer.usecase.UpdateCustomerUseCase;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UpdateCustomerCommandHandler implements CommandHandler<UpdateCustomerCommand, CustomerUpdatedResponse> {

    private final UpdateCustomerUseCase updateCustomerUseCase;

    public UpdateCustomerCommandHandler(UpdateCustomerUseCase updateCustomerUseCase) {
        this.updateCustomerUseCase = updateCustomerUseCase;
    }

    @Override
    public Mono<CustomerUpdatedResponse> handle(UpdateCustomerCommand command) {
        return this.updateCustomerUseCase.updatedCustomer(command);
    }
}