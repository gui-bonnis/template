package com.soul.fin.common.application.policy.registry;

import com.soul.fin.common.application.policy.AsyncPolicy;
import com.soul.fin.common.application.policy.Policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
public final class DefaultPolicyRegistry implements PolicyRegistry {

    private final Map<Class<?>, List<Policy<?>>> registry = new HashMap<>();
    private final Map<Class<?>, List<AsyncPolicy<?>>> eventPolicies = new HashMap<>();

    public <C> void register(
            Class<C> commandType,
            Policy<C> policy
    ) {
        registry
                .computeIfAbsent(commandType, k -> new ArrayList<>())
                .add(policy);
    }

    @Override
    public List<Policy<?>> policiesFor(Class<?> commandType) {
        return registry.getOrDefault(commandType, List.of());
    }

    public <E> void registerEventPolicy(
            Class<E> eventType,
            AsyncPolicy<E> policy
    ) {
        eventPolicies
                .computeIfAbsent(eventType, k -> new ArrayList<>())
                .add(policy);
    }

    @Override
    public List<AsyncPolicy<?>> policiesForEvent(Class<?> eventType) {
        return eventPolicies.getOrDefault(eventType, List.of());
    }
}

