package com.soul.fin.common.application.policy.effects;

import com.soul.fin.common.application.policy.Severity;

import java.time.Instant;

public record PolicyAlert(
        String code,
        String message,
        Severity severity,
        Instant occurredAt
) {
}

