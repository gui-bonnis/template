package com.soul.fin.server.customer.usecase;

import com.soul.fin.server.customer.dto.command.CustomerUpdatedResponse;
import com.soul.fin.server.customer.dto.command.UpdateCustomerCommand;
import com.soul.fin.server.customer.exception.CustomerApplicationExceptions;
import com.soul.fin.server.customer.mapper.CustomerMapper;
import com.soul.fin.server.customer.ports.output.repository.CustomerRepository;
import com.soul.fin.server.customer.validator.UpdateCustomerCommandValidator;
import com.soul.fin.service.customer.service.CustomerDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class UpdateCustomerUseCase {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;

    public UpdateCustomerUseCase(CustomerDomainService customerDomainService,
                                 CustomerRepository customerRepository) {
        this.customerDomainService = customerDomainService;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Mono<CustomerUpdatedResponse> updatedCustomer(UpdateCustomerCommand command) {

        return Mono.just(command)
                // validate command
                .transform(UpdateCustomerCommandValidator.validate())
                // get existing entity
                .flatMap(cmd -> customerRepository.findById(cmd.customerId()))
                // throw if not found
                .switchIfEmpty(CustomerApplicationExceptions.customerNotFound(command.customerId()))
                // call domain service
                .map(c -> customerDomainService.updateCustomer(c, CustomerMapper.toCustomer(command)))
                // save domain
                .flatMap(customerRepository::save)
                // call saga orchestrator service if needed (save saga resource)
                // publish event
                // outbox pattern
                // build return
                .map(CustomerMapper::toCustomerUpdatedResponse);
    }

}
