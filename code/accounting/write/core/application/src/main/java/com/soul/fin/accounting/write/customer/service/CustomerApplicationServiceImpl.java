package com.soul.fin.accounting.write.customer.service;

import com.soul.fin.accounting.write.customer.dto.command.*;
import com.soul.fin.accounting.write.customer.ports.input.service.CustomerApplicationService;
import com.soul.fin.accounting.write.customer.validator.DeleteCustomerCommandValidator;
import com.soul.fin.accounting.write.customer.validator.RegisterCustomerCommandValidator;
import com.soul.fin.accounting.write.customer.validator.UpdateCustomerCommandValidator;
import com.soul.fin.common.bus.SpringCommandBus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerApplicationServiceImpl implements CustomerApplicationService {

    private final SpringCommandBus commandBus;

    public CustomerApplicationServiceImpl(SpringCommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @Override
    public Mono<CustomerRegisteredResponse> registerCustomer(Mono<RegisterCustomerCommand> command) {
        return command
                // validate command properties
                .transform(RegisterCustomerCommandValidator.validate())
                // execute command
                .flatMap(commandBus::execute);
    }

    @Override
    public Mono<CustomerUpdatedResponse> updateCustomer(Mono<UpdateCustomerCommand> command) {
        return command
                // validate command properties
                .transform(UpdateCustomerCommandValidator.validate())
                // execute command
                .flatMap(commandBus::execute);
    }

    @Override
    public Mono<Void> deleteCustomer(Mono<DeleteCustomerCommand> command) {
        return command
                // validate command properties
                .transform(DeleteCustomerCommandValidator.validate())
                // execute command
                .flatMap(commandBus::execute);
    }

}
