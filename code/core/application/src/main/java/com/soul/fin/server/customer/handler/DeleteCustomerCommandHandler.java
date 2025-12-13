package com.soul.fin.server.customer.handler;


import com.soul.fin.common.bus.core.CommandHandler;
import com.soul.fin.server.customer.dto.command.DeleteCustomerCommand;
import com.soul.fin.server.customer.usecase.DeleteCustomerUseCase;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DeleteCustomerCommandHandler implements CommandHandler<DeleteCustomerCommand, Void> {

    private final DeleteCustomerUseCase deleteCustomerUseCase;

    public DeleteCustomerCommandHandler(DeleteCustomerUseCase deleteCustomerUseCase) {
        this.deleteCustomerUseCase = deleteCustomerUseCase;
    }

    @Override
    public Mono<Void> handle(DeleteCustomerCommand command) {
        return deleteCustomerUseCase.deleteCustomer(command);
    }
}