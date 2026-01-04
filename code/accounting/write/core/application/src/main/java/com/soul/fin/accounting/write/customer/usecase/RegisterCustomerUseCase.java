package com.soul.fin.accounting.write.customer.usecase;

import com.soul.fin.accounting.write.customer.dto.command.CustomerRegisteredResponse;
import com.soul.fin.accounting.write.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.accounting.write.customer.entity.Customer;
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
public class RegisterCustomerUseCase extends UseCase<CustomerId, Customer> {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;

    public RegisterCustomerUseCase(EventSourcedService<CustomerId, Customer> eventSourcedService,
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
//                // save event at event store
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
