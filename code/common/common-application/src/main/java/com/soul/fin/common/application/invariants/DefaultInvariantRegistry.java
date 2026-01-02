package com.soul.fin.common.application.invariants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DefaultInvariantRegistry
        implements InvariantRegistry {

    private final Map<Class<?>, List<InvariantValidator<?>>> registry =
            new HashMap<>();

    public <T> void register(
            Class<T> aggregateType,
            InvariantValidator<T> invariant
    ) {
        registry
                .computeIfAbsent(aggregateType, k -> new ArrayList<>())
                .add(invariant);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<InvariantValidator<T>> invariantsFor(
            Class<T> aggregateType
    ) {
        return (List<InvariantValidator<T>>)
                (List<?>) registry.getOrDefault(
                        aggregateType,
                        List.of()
                );
    }
}

