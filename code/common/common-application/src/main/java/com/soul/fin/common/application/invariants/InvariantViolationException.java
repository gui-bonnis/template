package com.soul.fin.common.application.invariants;

import java.util.List;
import java.util.stream.Collectors;

public final class InvariantViolationException extends RuntimeException {

    private final List<Violation> violations;

    public InvariantViolationException(List<Violation> violations) {
        super(buildMessage(violations));
        this.violations = List.copyOf(violations);
    }

    public List<Violation> violations() {
        return violations;
    }

    private static String buildMessage(List<Violation> violations) {
        return violations.stream()
                .map(v -> v.invariantName() + " [" + v.code() + "]: " + v.message())
                .collect(Collectors.joining("; "));
    }
}

