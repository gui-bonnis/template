package com.soul.fin.accounting.customer.usecase;

import com.soul.fin.accounting.customer.dto.command.CustomerRegisteredResponse;
import com.soul.fin.accounting.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.accounting.customer.mapper.CustomerMapper;
import com.soul.fin.accounting.customer.ports.output.repository.CustomerEventRepository;
import com.soul.fin.accounting.customer.ports.output.repository.CustomerRepository;
import com.soul.fin.accounting.customer.service.CustomerDomainService;
import com.soul.fin.accounting.customer.validator.RegisterCustomerCommandValidator;
import com.soul.fin.common.core.event.EventMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
public class RegisterCustomerUseCase {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;
    private final CustomerEventRepository eventRepository;

    public RegisterCustomerUseCase(CustomerDomainService customerDomainService,
                                   CustomerRepository customerRepository, CustomerEventRepository eventRepository) {
        this.customerDomainService = customerDomainService;
        this.customerRepository = customerRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Mono<CustomerRegisteredResponse> registerCustomer(RegisterCustomerCommand command) {

        return Mono.just(command)
                // validate command
                .transform(RegisterCustomerCommandValidator.validate())
                // map to domain entity
                .map(CustomerMapper::toCustomer)
                // call domain service
                .map(customerDomainService::registerCustomer)
                // save domain
                // call saga orchestrator service
                // save saga resource
                .flatMap(customerRepository::save)
                // save event at event store
                .map(customer -> {
                    EventMetadata metadata = buildMetadata();
                    eventRepository.append(customer, metadata);
                    return customer;
                })
                // publish event
                // outbox pattern
                // build return
                .map(CustomerMapper::toCustomerRegisteredResponse);
    }

    private EventMetadata buildMetadata() {
        var metadata = new EventMetadata();
        eventId(metadata); //Generate for each event
        correlationId(metadata); // get from command
        causationId(metadata); // not sure yet
        commandId(metadata); // get from command
        actor(metadata); // get from command???
        tenantId(metadata);
        schemaVersion(metadata);
        occurredAt(metadata);
        return metadata;
    }

    private void eventId(EventMetadata metadata) {
        metadata.setEventId(UUID.randomUUID());
    }

    private void correlationId(EventMetadata metadata) {
        metadata.setCorrelationId(UUID.randomUUID());
    }

    private void causationId(EventMetadata metadata) {
        metadata.setCausationId(UUID.randomUUID());
    }

    private void commandId(EventMetadata metadata) {
        metadata.setCommandId(UUID.randomUUID());
    }

    private void actor(EventMetadata metadata) {
        metadata.setActor("user | system");
    }

    private void tenantId(EventMetadata metadata) {
        metadata.setTenantId("Optional");
    }

    private void schemaVersion(EventMetadata metadata) {
        metadata.setSchemaVersion(1L); //TODO: add version to EventFromStore
    }

    private void occurredAt(EventMetadata metadata) {
        metadata.setOccurredAt(Instant.now());
    }


}
