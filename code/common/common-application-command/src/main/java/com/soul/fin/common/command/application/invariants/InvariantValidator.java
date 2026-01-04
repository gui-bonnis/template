package com.soul.fin.common.command.application.invariants;

public interface InvariantValidator<T> {

    ValidationResult validate(
            AggregateCandidate<T> candidate
    );
}