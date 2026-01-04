package com.soul.fin.common.command.application.invariants;

public record Violation(
        String invariantName,
        String code,
        String message
) {
}

