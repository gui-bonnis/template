package com.soul.fin.common.application.invariants;

public record Violation(
        String invariantName,
        String code,
        String message
) {
}

