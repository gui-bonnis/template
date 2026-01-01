package com.soul.fin.accounting.customer.usecase;

import com.soul.fin.accounting.customer.dto.command.DeleteCustomerCommand;
import com.soul.fin.accounting.customer.entity.Customer;
import com.soul.fin.accounting.customer.exception.CustomerApplicationExceptions;
import com.soul.fin.accounting.customer.mapper.CustomerMapper;
import com.soul.fin.accounting.customer.ports.output.repository.CustomerRepository;
import com.soul.fin.accounting.customer.service.CustomerDomainService;
import com.soul.fin.accounting.customer.validator.DeleteCustomerCommandValidator;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

//@Service
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
                // map command to domain
                .map(CustomerMapper::toCustomer)
                // execute delete
                .flatMap(this::execute);
    }

    private Mono<Void> execute(Customer customer) {
        // get existing entity
        return customerRepository.findById(customer.getId().getValue())
                .switchIfEmpty(CustomerApplicationExceptions.customerNotFound(customer.getId().getValue()))
                // call domain service for any business rules
                .map(customerDomainService::deleteCustomer)
                // delete domain
                .flatMap(c -> {
                    return customerRepository.deleteById(c.getId().getValue());
                    // call saga orchestrator service if needed (save saga resource)
                    // publish event
                    // .doOnNext()
                    // outbox pattern
                    // .doOnNext()
                })
                .then();
    }

}
