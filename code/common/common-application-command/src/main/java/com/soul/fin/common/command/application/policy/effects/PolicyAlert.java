package com.soul.fin.common.command.application.policy.effects;

import com.soul.fin.common.command.application.policy.Severity;

import java.time.Instant;

public record PolicyAlert(
        String code,
        String message,
        Severity severity,
        Instant occurredAt
) {
}

