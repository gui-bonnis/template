package com.soul.fin.common.application.policy.engine;

import com.soul.fin.common.application.policy.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public final class DefaultSyncPolicyEngine
        implements SyncPolicyEngine {

    @Override
    @SuppressWarnings("unchecked")
    public <C> PolicyEvaluationResult evaluate(
            C command,
            List<Policy<?>> policies,
            PolicyContext<C> context
    ) {
        List<PolicyTrace> traces = new ArrayList<>();

        for (Policy<?> rawPolicy : policies) {

            Policy<C> policy = (Policy<C>) rawPolicy;

            long start = System.nanoTime();
            PolicyDecision decision = policy.evaluate(context);
            long end = System.nanoTime();

            traces.add(new PolicyTrace(
                    policy.metadata().name(),
                    decision,
                    Duration.ofNanos(end - start)
            ));

            if (decision instanceof PolicyDecision.Deny deny
                    && deny.severity() == Severity.HARD_BLOCK) {
                return new PolicyEvaluationResult(false, traces);
            }
        }

        return new PolicyEvaluationResult(true, traces);
    }
}




