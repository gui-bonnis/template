package com.soul.fin.server.customer.ports.input.service;

import com.soul.fin.server.customer.dto.command.CustomerRegisteredResponse;
import com.soul.fin.server.customer.dto.command.RegisterCustomerCommand;
import reactor.core.publisher.Mono;

public interface CustomerApplicationService {

    Mono<CustomerRegisteredResponse> handle(Mono<RegisterCustomerCommand> command);

}
