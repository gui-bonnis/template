package com.soul.fin.accounting.customer.usecase;

import com.soul.fin.accounting.customer.dto.command.CustomerUpdatedResponse;
import com.soul.fin.accounting.customer.dto.command.UpdateCustomerCommand;
import com.soul.fin.accounting.customer.entity.Customer;
import com.soul.fin.accounting.customer.exception.CustomerApplicationExceptions;
import com.soul.fin.accounting.customer.mapper.CustomerMapper;
import com.soul.fin.accounting.customer.ports.output.repository.CustomerRepository;
import com.soul.fin.accounting.customer.service.CustomerDomainService;
import com.soul.fin.accounting.customer.vo.CustomerId;
import com.soul.fin.common.application.dto.AggregateExecution;
import com.soul.fin.common.application.invariants.InvariantGuard;
import com.soul.fin.common.application.policy.engine.DefaultSyncPolicyEngine;
import com.soul.fin.common.application.policy.registry.PolicyRegistry;
import com.soul.fin.common.application.policy.risk.RiskService;
import com.soul.fin.common.application.policy.service.PolicyServices;
import com.soul.fin.common.application.ports.output.publisher.EventPublisher;
import com.soul.fin.common.application.ports.output.publisher.MessagePublisher;
import com.soul.fin.common.application.service.EventSourcedService;
import com.soul.fin.common.application.usecase.UseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class UpdateCustomerUseCase extends UseCase<CustomerId, Customer> {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;

    public UpdateCustomerUseCase(EventSourcedService<CustomerId, Customer> eventSourcedService,
                                 EventPublisher eventPublisher,
                                 MessagePublisher messagePublisher,
                                 InvariantGuard invariantGuard,
                                 DefaultSyncPolicyEngine policyEngine,
                                 PolicyRegistry policyRegistry,
                                 PolicyServices policyServices,
                                 RiskService riskService,
                                 CustomerDomainService customerDomainService,
                                 CustomerRepository customerRepository
    ) {
        super(eventSourcedService, eventPublisher, messagePublisher, invariantGuard,
                policyEngine, policyRegistry, policyServices, riskService);
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
