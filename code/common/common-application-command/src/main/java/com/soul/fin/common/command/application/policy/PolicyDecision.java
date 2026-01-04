package com.soul.fin.common.command.application.policy;

public sealed interface PolicyDecision {

    record Allow() implements PolicyDecision {
    }

    record Deny(
            String code,
            String reason,
            Severity severity
    ) implements PolicyDecision {
    }

    record Warn(
            String code,
            String message
    ) implements PolicyDecision {
    }
}


