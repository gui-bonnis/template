package com.soul.fin.common.command.application.policy;

import com.soul.fin.common.command.application.policy.effects.PolicyEffects;
import com.soul.fin.common.command.application.policy.service.PolicyServices;

import java.time.Instant;

public record PolicyContext<I>(
        I input,
        Instant now,
        PolicyExecutionMode mode,
        PolicyServices services,
        PolicyEffects effects
) {
}

