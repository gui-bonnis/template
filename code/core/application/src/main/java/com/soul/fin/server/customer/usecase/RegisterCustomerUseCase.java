package com.soul.fin.server.customer.usecase;

import com.soul.fin.server.customer.dto.command.CustomerRegisteredResponse;
import com.soul.fin.server.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.server.customer.mapper.CustomerMapper;
import com.soul.fin.server.customer.ports.output.repository.CustomerRepository;
import com.soul.fin.service.customer.service.CustomerDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class RegisterCustomerUseCase {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;

    public RegisterCustomerUseCase(CustomerDomainService customerDomainService,
                                   CustomerRepository customerRepository) {
        this.customerDomainService = customerDomainService;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Mono<CustomerRegisteredResponse> registerCustomer(RegisterCustomerCommand command) {

        return Mono.just(command)
                // validate entities within command
                // map to use case input
                .flatMap(CustomerMapper::toCustomer)
                // call domain service
                .map(customerDomainService::registerCustomer)
                // call saga orchestrator service if needed (save saga resource)
                // save domain
                .flatMap(customerRepository::save)
                // publish event
                // outbox pattern
                // build return
                .map(CustomerMapper::toResponse);
    }

}
