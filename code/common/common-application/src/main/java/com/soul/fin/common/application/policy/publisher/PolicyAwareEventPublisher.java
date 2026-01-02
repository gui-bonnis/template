package com.soul.fin.common.application.policy.publisher;

import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.application.policy.AsyncPolicy;
import com.soul.fin.common.application.policy.PolicyContext;
import com.soul.fin.common.application.policy.PolicyExecutionMode;
import com.soul.fin.common.application.policy.effects.NoOpPolicyEffects;
import com.soul.fin.common.application.policy.engine.AsyncPolicyEngine;
import com.soul.fin.common.application.policy.registry.PolicyRegistry;
import com.soul.fin.common.application.policy.service.PolicyServices;
import com.soul.fin.common.application.ports.output.publisher.EventPublisher;
import com.soul.fin.common.core.event.DomainEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Component
public final class PolicyAwareEventPublisher implements EventPublisher {

    private final AsyncPolicyEngine policyEngine;
    private final PolicyRegistry registry;
    private final PolicyServices policyServices;

    public PolicyAwareEventPublisher(
            AsyncPolicyEngine policyEngine,
            PolicyRegistry registry, PolicyServices policyServices) {
        this.policyEngine = policyEngine;
        this.registry = registry;
        this.policyServices = policyServices;
    }

    @Override
    public Mono<Void> publish(List<EventEnvelope> events) {
        for (EventEnvelope event : events) {
            List<AsyncPolicy<?>> policies =
                    registry.policiesForEvent(event.payload().getClass());

            if (policies.isEmpty())
                continue;

            PolicyContext<DomainEvent> context =
                    new PolicyContext<>(
                            event.payload(),
                            Instant.now(),
                            PolicyExecutionMode.ASYNC,
                            policyServices,
                            new NoOpPolicyEffects()
                    );

            policyEngine.dispatch(event.payload(), policies, context);
        }
        return Mono.empty();
    }
}

