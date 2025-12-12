package com.soul.fin.server.customer;

import com.soul.fin.server.customer.dto.command.CustomerRegisteredResponse;
import com.soul.fin.server.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.server.customer.dto.query.CustomerQuery;
import com.soul.fin.server.customer.dto.query.GetCustomerByIdQuery;
import com.soul.fin.server.customer.handler.GetCustomerByIdQueryHandler;
import com.soul.fin.server.customer.handler.RegisterCustomerCommandHandler;
import com.soul.fin.server.customer.ports.input.service.CustomerApplicationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerApplicationServiceImpl implements CustomerApplicationService {

    public final RegisterCustomerCommandHandler registerCustomerCommandHandler;
    public final GetCustomerByIdQueryHandler getCustomerByIdQueryHandler;

    public CustomerApplicationServiceImpl(RegisterCustomerCommandHandler registerCustomerCommandHandler, GetCustomerByIdQueryHandler getCustomerByIdQueryHandler) {
        this.registerCustomerCommandHandler = registerCustomerCommandHandler;
        this.getCustomerByIdQueryHandler = getCustomerByIdQueryHandler;
    }

    @Override
    public Mono<CustomerRegisteredResponse> registerCustomer(Mono<RegisterCustomerCommand> command) {
        return command.flatMap(registerCustomerCommandHandler::handle);
    }

    @Override
    public Mono<CustomerQuery> getCustomerById(Mono<GetCustomerByIdQuery> query) {
        return query.flatMap(getCustomerByIdQueryHandler::handle);
    }


}
