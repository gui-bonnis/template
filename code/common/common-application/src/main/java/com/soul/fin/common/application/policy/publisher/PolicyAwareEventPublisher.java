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
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

//@Component
public final class PolicyAwareEventPublisher implements EventPublisher {

    private final EventPublisher delegate;
    private final AsyncPolicyEngine policyEngine;
    private final PolicyRegistry registry;
    private final PolicyServices policyServices;

    public PolicyAwareEventPublisher(EventPublisher delegate, AsyncPolicyEngine policyEngine, PolicyRegistry registry, PolicyServices policyServices) {
        this.delegate = delegate;
        this.policyEngine = policyEngine;
        this.registry = registry;
        this.policyServices = policyServices;
    }

    @Override
    public Mono<Void> publish(List<EventEnvelope> events) {

        delegate.publish(events);

        for (EventEnvelope event : events) {
            List<AsyncPolicy<?>> policies =
                    registry.policiesForEvent(event.getClass());

            PolicyContext<EventEnvelope> context =
                    new PolicyContext<>(
                            event,
                            Instant.now(),
                            PolicyExecutionMode.ASYNC,
                            policyServices,
                            new NoOpPolicyEffects()
                    );

            policyEngine.dispatch(event, policies, context);
        }

        return Mono.empty();
    }
}

