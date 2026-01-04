package com.soul.fin.accounting.write.customer.usecase;

import com.soul.fin.accounting.write.customer.dto.command.CustomerUpdatedResponse;
import com.soul.fin.accounting.write.customer.dto.command.UpdateCustomerCommand;
import com.soul.fin.accounting.write.customer.entity.Customer;
import com.soul.fin.accounting.write.customer.exception.CustomerApplicationExceptions;
import com.soul.fin.accounting.write.customer.mapper.CustomerMapper;
import com.soul.fin.accounting.write.customer.ports.output.repository.CustomerRepository;
import com.soul.fin.accounting.write.customer.service.CustomerDomainService;
import com.soul.fin.accounting.write.customer.vo.CustomerId;
import com.soul.fin.common.application.dto.AggregateExecution;
import com.soul.fin.common.application.event.EventPipeline;
import com.soul.fin.common.command.application.invariants.InvariantGuard;
import com.soul.fin.common.command.application.policy.engine.DefaultSyncPolicyEngine;
import com.soul.fin.common.command.application.policy.registry.PolicyRegistry;
import com.soul.fin.common.command.application.policy.service.PolicyServices;
import com.soul.fin.common.command.application.service.EventSourcedService;
import com.soul.fin.common.command.application.usecase.UseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class UpdateCustomerUseCase extends UseCase<CustomerId, Customer> {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;

    public UpdateCustomerUseCase(EventSourcedService<CustomerId, Customer> eventSourcedService,
                                 EventPipeline eventPipeline,
                                 InvariantGuard invariantGuard,
                                 DefaultSyncPolicyEngine policyEngine,
                                 PolicyRegistry policyRegistry,
                                 PolicyServices policyServices,
                                 CustomerDomainService customerDomainService,
                                 CustomerRepository customerRepository
    ) {
        super(eventSourcedService, eventPipeline, invariantGuard,
                policyEngine, policyRegistry, policyServices);
        this.customerDomainService = customerDomainService;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Mono<CustomerUpdatedResponse> updatedCustomer(UpdateCustomerCommand command) {

        return Mono.just(command)
                // get existing entity
                .flatMap(cmd -> this.load(new CustomerId(cmd.customerId())))
                // throw if not found
                .switchIfEmpty(CustomerApplicationExceptions.customerNotFound(command.customerId()))
                // evaluate sync polices
                .flatMap(aggregate -> evaluatePolicies(aggregate, command))
                // call domain service
                .map(c -> customerDomainService.updateCustomer(c, CustomerMapper.toCustomer(command)))
                // pull events
                .map(agg -> new AggregateExecution<>(agg, agg.pullEvents()))
                // invariant validations
                .flatMap(this::validateInvariants)
                //build envelop
                .flatMap(exec -> this.buildEnvelopes(exec, command))
                // save domain
                .flatMap(exec -> customerRepository.update(exec.aggregate())
                        .thenReturn(exec))
                // call saga orchestrator service
                // save saga resource
                //.flatMap(customerRepository::save)
                // save event at event store
                .flatMap(this::saveEvent)
                // publish event
                .flatMap(this::publishEvent)
                // build return
                .map(exec -> CustomerMapper.toCustomerUpdatedResponse(exec.aggregate()));
    }

}
