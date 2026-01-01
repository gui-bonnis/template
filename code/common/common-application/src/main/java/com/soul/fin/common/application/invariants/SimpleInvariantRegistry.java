package com.soul.fin.common.application.invariants;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleInvariantRegistry implements InvariantRegistry {

    @Override
    public <T> List<InvariantValidator<T>> invariantsFor(Class<T> aggregateType) {
        return List.of(); // No invariants for now
    }
}