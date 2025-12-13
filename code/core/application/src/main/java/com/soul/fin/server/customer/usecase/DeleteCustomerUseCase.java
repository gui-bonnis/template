package com.soul.fin.server.customer.usecase;

import com.soul.fin.server.customer.dto.command.DeleteCustomerCommand;
import com.soul.fin.server.customer.exception.CustomerApplicationExceptions;
import com.soul.fin.server.customer.mapper.CustomerMapper;
import com.soul.fin.server.customer.ports.output.repository.CustomerRepository;
import com.soul.fin.server.customer.validator.DeleteCustomerCommandValidator;
import com.soul.fin.service.customer.entity.Customer;
import com.soul.fin.service.customer.service.CustomerDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class DeleteCustomerUseCase {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;

    public DeleteCustomerUseCase(CustomerDomainService customerDomainService,
                                 CustomerRepository customerRepository) {
        this.customerDomainService = customerDomainService;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Mono<Void> deleteCustomer(DeleteCustomerCommand command) {

        return Mono.just(command)
                // validate command
                .transform(DeleteCustomerCommandValidator.validate())
                // validate entities within command
                // map to use case input
                .map(CustomerMapper::toCustomer)
                // call domain service for any business rules
                .map(customerDomainService::deleteCustomer)
                // execute delete
                .flatMap(this::execute);
    }

    private Mono<Void> execute(Customer customer) {
        return customerRepository.findById(customer.getId().value())
                .switchIfEmpty(CustomerApplicationExceptions.customerNotFound(customer.getId().value()))
                .flatMap(c ->
                {
                    return customerRepository.deleteById(c.getId().value());
                    // call saga orchestrator service if needed (save saga resource)
                    // publish event
                    // .doOnNext()
                    // outbox pattern
                    // .doOnNext()
                })
                .then();
    }

}
