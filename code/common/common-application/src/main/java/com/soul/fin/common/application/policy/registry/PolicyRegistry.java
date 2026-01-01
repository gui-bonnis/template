package com.soul.fin.common.application.policy.registry;

import com.soul.fin.common.application.policy.AsyncPolicy;
import com.soul.fin.common.application.policy.Policy;

import java.util.List;

public interface PolicyRegistry {

    List<Policy<?>> policiesFor(Class<?> commandType);

    List<AsyncPolicy<?>> policiesForEvent(Class<?> eventType);
}

