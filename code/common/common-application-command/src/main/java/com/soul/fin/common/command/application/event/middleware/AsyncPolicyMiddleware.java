package com.soul.fin.common.command.application.event.middleware;

import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.application.event.EventMiddleware;
import com.soul.fin.common.application.event.EventMiddlewareChain;
import com.soul.fin.common.command.application.policy.PolicyContext;
import com.soul.fin.common.command.application.policy.PolicyExecutionMode;
import com.soul.fin.common.command.application.policy.effects.NoOpPolicyEffects;
import com.soul.fin.common.command.application.policy.engine.AsyncPolicyEngine;
import com.soul.fin.common.command.application.policy.registry.PolicyRegistry;
import com.soul.fin.common.command.application.policy.service.PolicyServices;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Component
public final class AsyncPolicyMiddleware implements EventMiddleware {

    private final AsyncPolicyEngine engine;
    private final PolicyRegistry registry;
    private final PolicyServices services;

    public AsyncPolicyMiddleware(AsyncPolicyEngine engine, PolicyRegistry registry, PolicyServices services) {
        this.engine = engine;
        this.registry = registry;
        this.services = services;
    }

    @Override
    public Mono<Void> handle(
            List<EventEnvelope> events,
            EventMiddlewareChain next
    ) {
        for (EventEnvelope envelope : events) {
            Object event = envelope.payload();

            var policies =
                    registry.policiesForEvent(event.getClass());

            if (policies.isEmpty()) continue;

            PolicyContext<Object> ctx =
                    new PolicyContext<>(
                            event,
                            Instant.now(),
                            PolicyExecutionMode.ASYNC,
                            services,
                            new NoOpPolicyEffects()
                    );

            engine.dispatch(event, policies, ctx);
        }

        return next.proceed(events);
    }
}

