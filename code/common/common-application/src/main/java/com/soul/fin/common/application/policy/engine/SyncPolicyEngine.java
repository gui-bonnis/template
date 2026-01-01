package com.soul.fin.common.application.policy.engine;

import com.soul.fin.common.application.policy.Policy;
import com.soul.fin.common.application.policy.PolicyContext;
import com.soul.fin.common.application.policy.PolicyEvaluationResult;

import java.util.List;

public interface SyncPolicyEngine {

    <C> PolicyEvaluationResult evaluate(
            C command,
            List<Policy<?>> policies,
            PolicyContext<C> context
    );
}

