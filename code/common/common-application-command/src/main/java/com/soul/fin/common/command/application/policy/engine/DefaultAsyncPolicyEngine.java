package com.soul.fin.common.command.application.policy.engine;

import com.soul.fin.common.command.application.policy.AsyncPolicy;
import com.soul.fin.common.command.application.policy.PolicyContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class DefaultAsyncPolicyEngine implements AsyncPolicyEngine {

    @Override
    @SuppressWarnings("unchecked")
    public <E> void dispatch(
            E event,
            List<AsyncPolicy<?>> policies,
            PolicyContext<E> context
    ) {
        for (AsyncPolicy<?> rawPolicy : policies) {
            AsyncPolicy<E> policy = (AsyncPolicy<E>) rawPolicy;
            policy.onEvent(event, context);
        }
    }
}

