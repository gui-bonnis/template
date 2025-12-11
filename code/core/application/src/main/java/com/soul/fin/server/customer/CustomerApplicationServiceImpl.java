package com.soul.fin.server.customer;

import com.soul.fin.server.customer.dto.command.CustomerRegisteredResponse;
import com.soul.fin.server.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.server.customer.handler.RegisterCustomerCommandHandler;
import com.soul.fin.server.customer.ports.input.service.CustomerApplicationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerApplicationServiceImpl implements CustomerApplicationService {

    public final RegisterCustomerCommandHandler registerCustomerCommandHandler;

    public CustomerApplicationServiceImpl(RegisterCustomerCommandHandler registerCustomerCommandHandler) {
        this.registerCustomerCommandHandler = registerCustomerCommandHandler;
    }

    @Override
    public Mono<CustomerRegisteredResponse> handle(Mono<RegisterCustomerCommand> command) {
        return command.flatMap(registerCustomerCommandHandler::handle);
    }
}
