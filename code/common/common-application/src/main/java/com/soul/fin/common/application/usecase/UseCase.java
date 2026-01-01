package com.soul.fin.common.application.usecase;

import com.soul.fin.common.application.dto.AggregateExecution;
import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.application.dto.ExecutionContext;
import com.soul.fin.common.application.invariants.InvariantGuard;
import com.soul.fin.common.application.invariants.InvariantViolationException;
import com.soul.fin.common.application.invariants.ValidationResult;
import com.soul.fin.common.application.invariants.Violation;
import com.soul.fin.common.application.policy.Policy;
import com.soul.fin.common.application.policy.PolicyContext;
import com.soul.fin.common.application.policy.PolicyEvaluationResult;
import com.soul.fin.common.application.policy.PolicyExecutionMode;
import com.soul.fin.common.application.policy.effects.NoOpPolicyEffects;
import com.soul.fin.common.application.policy.engine.DefaultSyncPolicyEngine;
import com.soul.fin.common.application.policy.exception.PolicyViolationException;
import com.soul.fin.common.application.policy.feature.FeatureFlags;
import com.soul.fin.common.application.policy.registry.PolicyRegistry;
import com.soul.fin.common.application.policy.risk.RiskService;
import com.soul.fin.common.application.policy.service.DefaultPolicyServices;
import com.soul.fin.common.application.policy.service.PolicyServices;
import com.soul.fin.common.application.ports.output.publisher.EventPublisher;
import com.soul.fin.common.application.service.EventSourcedService;
import com.soul.fin.common.bus.core.Command;
import com.soul.fin.common.core.entity.BaseAggregateRoot;
import com.soul.fin.common.core.event.EventMetadata;
import com.soul.fin.common.core.vo.BaseId;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public abstract class UseCase<T extends BaseId<?>, A extends BaseAggregateRoot<T>> {

    private final EventSourcedService<T, A> eventSourcedService;
    private final EventPublisher publisher;
    private final InvariantGuard invariantGuard;
    //private final InvariantRegistry invariantRegistry;
    private final DefaultSyncPolicyEngine policyEngine;
    private final PolicyRegistry policyRegistry;

    private final FeatureFlags featureFlags = key -> true; // everything enabled for now
    private final RiskService riskService;

    protected EventSourcedService<T, A> getEventSourcedService() {
        return eventSourcedService;
    }

    protected UseCase(EventSourcedService<T, A> eventSourcedService, EventPublisher publisher,
                      InvariantGuard invariantGuard,
                      DefaultSyncPolicyEngine policyEngine, PolicyRegistry policyRegistry,
                      RiskService riskService) {
        this.eventSourcedService = eventSourcedService;
        this.publisher = publisher;
        this.invariantGuard = invariantGuard;
        this.policyEngine = policyEngine;
        this.policyRegistry = policyRegistry;
        this.riskService = riskService;
    }

    protected Mono<A> load(T id) {
        return eventSourcedService.load(id);
    }

    protected <C> void evaluatePolicies(C command) {

        List<Policy<?>> policies =
                policyRegistry.policiesFor(command.getClass());

        PolicyContext<C> context =
                new PolicyContext<>(
                        command,
                        Instant.now(),
                        PolicyExecutionMode.SYNC,
                        buildPolicyServices(),
                        new NoOpPolicyEffects()
                );

        PolicyEvaluationResult result =
                policyEngine.evaluate(command, policies, context);

        if (!result.allowed()) {
            throw new PolicyViolationException(result);
        }
    }

    protected PolicyServices buildPolicyServices() {
        return new DefaultPolicyServices(
                //readModels(),
                featureFlags,
                riskService,
                Clock.systemUTC()
        );
    }

    protected Mono<AggregateExecution<A>> validateInvariants(AggregateExecution<A> execution) {
        return Mono.defer(() -> {
            ValidationResult result =
                    invariantGuard.validate(execution.aggregate(), execution.events());

            if (result instanceof ValidationResult.Invalid(
                    java.util.List<Violation> violations
            )) {
                return Mono.error(
                        new InvariantViolationException(violations)
                );
            }

            return Mono.just(execution);
        });
    }

    protected Mono<ExecutionContext<A>> buildEnvelopes(AggregateExecution<A> exec, Command<?> command) {

        var executionContext = new ExecutionContext<>(exec.aggregate(),
                exec.events().stream()
                        .map(event ->
                                new EventEnvelope(
                                        UUID.randomUUID(),
                                        event.getClass().getSimpleName(),
                                        event.aggregateId(),
                                        exec.aggregate().getClass().getSimpleName(),
                                        exec.aggregate().getAggregateVersion(),
                                        event,
                                        buildMetadata(command, event.eventSchemaVersion())
                                ))
                        .toList()
        );

        return Mono.just(executionContext);
    }

    private EventMetadata buildMetadata(Command<?> command, long eventSchemaVersion) {
        var metadata = new EventMetadata();
        metadata.setEventId(UUID.randomUUID());
        metadata.setCorrelationId(command.metadata().getCorrelationId());
        metadata.setCausationId(command.metadata().getCausationId());
        metadata.setCommandId(command.metadata().getCommandId());
        metadata.setActor(command.metadata().getActor());
        metadata.setTenantId(command.metadata().getTenantId());
        metadata.setSchemaVersion(eventSchemaVersion); //TODO: add version to EventFromStore, check if it is aggSchema or version
        metadata.setOccurredAt(Instant.now());
        return metadata;
    }

    protected Mono<ExecutionContext<A>> save(ExecutionContext<A> exec, Command<?> command) {
        return this.getEventSourcedService()
                .save(exec.envelopes())
                .thenReturn(exec);
    }

    protected Mono<ExecutionContext<A>> publish(ExecutionContext<A> exec, Command<?> command) {
        return this.publisher
                .publish(exec.envelopes())
                .thenReturn(exec);
    }

//    protected Mono<AggregateExecution<A>> save(AggregateExecution<A> exec, Command<?> command) {
//        return this.getEventSourcedService()
//                .save(buildEnvelopes(exec.aggregate(), command))
//                .thenReturn(exec);
//    }

//    protected Flux<EventEnvelope> buildEnvelopes(A aggregateRoot, Command<?> command) {
//        return Flux.fromIterable(aggregateRoot.pullEvents())
//                .map(event ->
//                        new EventEnvelope(
//                                UUID.randomUUID(),
//                                event.getClass().getSimpleName(),
//                                event.aggregateId(),
//                                aggregateRoot.getClass().getSimpleName(),
//                                aggregateRoot.getAggregateVersion(),
//                                event,
//                                buildMetadata(command, event.eventSchemaVersion())
//                        ));
//    }

//
//    protected Mono<AggregateExecution<A>> publish(AggregateExecution<A> exec, Command<?> command) {
//        return this.publisher
//                .publish(buildEnvelopes(exec.aggregate(), command))
//                .thenReturn(exec);
//    }

}
