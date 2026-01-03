package com.soul.fin.accounting.write.customer.ports.input.service;

import com.soul.fin.accounting.write.customer.dto.command.*;
import reactor.core.publisher.Mono;

public interface CustomerApplicationService {

    Mono<CustomerRegisteredResponse> registerCustomer(Mono<RegisterCustomerCommand> command);

    Mono<CustomerUpdatedResponse> updateCustomer(Mono<UpdateCustomerCommand> command);

    Mono<Void> deleteCustomer(Mono<DeleteCustomerCommand> command);


}
