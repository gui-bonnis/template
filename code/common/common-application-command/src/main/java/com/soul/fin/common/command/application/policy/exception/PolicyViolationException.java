package com.soul.fin.common.command.application.policy.exception;

import com.soul.fin.common.command.application.policy.PolicyEvaluationResult;

public final class PolicyViolationException
        extends RuntimeException {

    private final PolicyEvaluationResult result;

    public PolicyViolationException(PolicyEvaluationResult result) {
        super("Policy violation");
        this.result = result;
    }

    public PolicyEvaluationResult result() {
        return result;
    }
}

