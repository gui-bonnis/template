package com.soul.fin.accounting.customer.invariants;

import com.soul.fin.common.application.invariants.*;
import com.soul.fin.common.core.event.DomainEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public final class DefaultInvariantGuard implements InvariantGuard {

    private final InvariantRegistry registry;
    private final AggregateCandidateFactory candidateFactory;

    public DefaultInvariantGuard(
            InvariantRegistry registry,
            AggregateCandidateFactory candidateFactory
    ) {
        this.registry = registry;
        this.candidateFactory = candidateFactory;
    }

    @Override
    public <T> ValidationResult validate(
            T aggregate,
            List<DomainEvent> newEvents
    ) {
        List<InvariantValidator<T>> invariants =
                registry.invariantsFor((Class<T>) aggregate.getClass());

        if (invariants.isEmpty()) {
            return new ValidationResult.Valid();
        }

        AggregateCandidate<T> candidate =
                candidateFactory.from(aggregate, newEvents);

        List<Violation> violations = new ArrayList<>();

        for (InvariantValidator<T> invariant : invariants) {
            ValidationResult result = invariant.validate(candidate);
            if (result instanceof ValidationResult.Invalid(List<Violation> violations1)) {
                violations.addAll(violations1);
            }
        }

        return violations.isEmpty()
                ? new ValidationResult.Valid()
                : new ValidationResult.Invalid(violations);
    }
}

