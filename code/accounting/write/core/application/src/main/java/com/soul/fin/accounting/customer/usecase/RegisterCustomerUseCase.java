package com.soul.fin.accounting.customer.usecase;

import com.soul.fin.accounting.customer.dto.command.CustomerRegisteredResponse;
import com.soul.fin.accounting.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.accounting.customer.entity.Customer;
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
public class RegisterCustomerUseCase extends UseCase<CustomerId, Customer> {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;

    public RegisterCustomerUseCase(EventSourcedService<CustomerId, Customer> eventSourcedService,
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
    public Mono<CustomerRegisteredResponse> registerCustomer(RegisterCustomerCommand command) {

        return Mono.just(command)
                // map to domain entity
                .map(CustomerMapper::toCustomer)
                // evaluate sync polices
                .flatMap(aggregate -> evaluatePolicies(aggregate, command))
                // call domain service
                .map(customerDomainService::registerCustomer)
                // pull events
                .map(agg -> new AggregateExecution<>(agg, agg.pullEvents()))
                // invariant validations
                .flatMap(this::validateInvariants)
                //build envelop
                .flatMap(exec -> this.buildEnvelopes(exec, command))
                // save domain
                .flatMap(exec -> customerRepository.insert(exec.aggregate())
                        .thenReturn(exec))
                // process saga
                //.flatMap(exec -> {
                // call saga orchestrator service
                // sagaService.execute(exec.aggregate());
                // save saga resource
                // customerSagaRepository.save(exec.aggregate());
                // return exec;
                // })
                // save event at event store
                .flatMap(this::saveEvent)
                // publish event
                .flatMap(this::publishEvent)
                // build return
                .map(exec -> CustomerMapper.toCustomerRegisteredResponse(exec.aggregate()))
                // Error handling
//                .onErrorResume(InvariantViolationException.class, ex ->
//                        Mono.error(
//                                //new BusinessRuleViolationException(ex.violations())
//                                new RuntimeException()
//                        )
//                )
                ;
    }

}
