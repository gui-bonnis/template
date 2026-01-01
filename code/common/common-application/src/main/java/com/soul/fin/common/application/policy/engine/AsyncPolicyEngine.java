package com.soul.fin.common.application.policy.engine;

import com.soul.fin.common.application.policy.AsyncPolicy;
import com.soul.fin.common.application.policy.PolicyContext;

import java.util.List;

public interface AsyncPolicyEngine {

    <E> void dispatch(
            E event,
            List<AsyncPolicy<?>> policies,
            PolicyContext<E> context
    );
}

