package com.soul.fin.common.application.invariants;

public interface InvariantValidator<T> {

    ValidationResult validate(
            AggregateCandidate<T> candidate
    );
}