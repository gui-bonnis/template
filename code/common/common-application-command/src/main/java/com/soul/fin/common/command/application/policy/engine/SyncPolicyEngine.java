package com.soul.fin.common.command.application.policy.engine;

import com.soul.fin.common.command.application.policy.Policy;
import com.soul.fin.common.command.application.policy.PolicyContext;
import com.soul.fin.common.command.application.policy.PolicyEvaluationResult;

import java.util.List;

public interface SyncPolicyEngine {

    <C> PolicyEvaluationResult evaluate(
            C command,
            List<Policy<?>> policies,
            PolicyContext<C> context
    );
}

