package com.soul.fin.accounting.write.customer.handler.command;


import com.soul.fin.accounting.write.customer.dto.command.DeleteCustomerCommand;
import com.soul.fin.accounting.write.customer.usecase.DeleteCustomerUseCase;
import com.soul.fin.common.bus.core.CommandHandler;
import reactor.core.publisher.Mono;

//@Component
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