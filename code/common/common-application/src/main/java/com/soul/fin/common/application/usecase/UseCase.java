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
import com.soul.fin.common.application.policy.registry.PolicyRegistry;
import com.soul.fin.common.application.policy.risk.RiskService;
import com.soul.fin.common.application.policy.service.PolicyServices;
import com.soul.fin.common.application.ports.output.publisher.EventPublisher;
import com.soul.fin.common.application.ports.output.publisher.MessagePublisher;
import com.soul.fin.common.application.service.EventSourcedService;
import com.soul.fin.common.bus.core.Command;
import com.soul.fin.common.core.entity.BaseAggregateRoot;
import com.soul.fin.common.core.event.EventMetadata;
import com.soul.fin.common.core.vo.BaseId;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public abstract class UseCase<T extends BaseId<?>, A extends BaseAggregateRoot<T>> {

    private final EventSourcedService<T, A> eventSourcedService;
    private final EventPublisher eventPublisher;
    private final MessagePublisher messagePublisher;
    private final InvariantGuard invariantGuard;
    private final DefaultSyncPolicyEngine policyEngine;
    private final PolicyRegistry policyRegistry;
    private final PolicyServices policyServices;
    private final RiskService riskService;

    protected EventSourcedService<T, A> getEventSourcedService() {
        return eventSourcedService;
    }

    protected UseCase(EventSourcedService<T, A> eventSourcedService,
                      EventPublisher eventPublisher,
                      MessagePublisher messagePublisher,
                      InvariantGuard invariantGuard,
                      DefaultSyncPolicyEngine policyEngine,
                      PolicyRegistry policyRegistry,
                      PolicyServices policyServices,
                      RiskService riskService) {
        this.eventSourcedService = eventSourcedService;
        this.eventPublisher = eventPublisher;
        this.messagePublisher = messagePublisher;
        this.invariantGuard = invariantGuard;
        this.policyEngine = policyEngine;
        this.policyRegistry = policyRegistry;
        this.policyServices = policyServices;
        this.riskService = riskService;
    }

    protected Mono<A> load(T id) {
        return eventSourcedService.load(id);
    }

    protected <C> Mono<A> evaluatePolicies(A aggregate, C command) {

        List<Policy<?>> policies =
                policyRegistry.policiesFor(command.getClass());

        PolicyContext<C> context =
                new PolicyContext<>(
                        command,
                        Instant.now(),
                        PolicyExecutionMode.SYNC,
                        this.policyServices,
                        new NoOpPolicyEffects()
                );

        PolicyEvaluationResult result =
                policyEngine.evaluate(command, policies, context);

        if (!result.allowed()) {
            throw new PolicyViolationException(result);
        }

        return Mono.just(aggregate);
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
                                        event.eventId(),
                                        event.getClass().getSimpleName(),
                                        event.aggregateId(),
                                        exec.aggregate().getClass().getSimpleName(),
                                        exec.aggregate().getAggregateVersion(),
                                        event,
                                        buildMetadata(command, event.eventId(), event.eventSchemaVersion())
                                ))
                        .toList()
        );

        return Mono.just(executionContext);
    }

    private EventMetadata buildMetadata(Command<?> command, UUID eventId, long eventSchemaVersion) {
        var metadata = new EventMetadata();
        metadata.setEventId(eventId);
        metadata.setCorrelationId(command.metadata().getCorrelationId());
        metadata.setCausationId(command.metadata().getCausationId());
        metadata.setCommandId(command.metadata().getCommandId());
        metadata.setActor(command.metadata().getActor());
        metadata.setTenantId(command.metadata().getTenantId());
        metadata.setSchemaVersion(eventSchemaVersion);
        metadata.setOccurredAt(Instant.now());
        return metadata;
    }

    protected Mono<ExecutionContext<A>> saveEvent(ExecutionContext<A> exec) {
        return this.getEventSourcedService()
                .save(exec.envelopes())
                .then(Mono.just(exec));
    }

    protected Mono<ExecutionContext<A>> publishEvent(ExecutionContext<A> exec) {
        return Mono.just(exec)
                .map(e -> this.eventPublisher.publish(e.envelopes()))
                .thenReturn(exec)
                .map(e -> this.messagePublisher.publish(e.envelopes()))
                .thenReturn(exec);
    }

}
