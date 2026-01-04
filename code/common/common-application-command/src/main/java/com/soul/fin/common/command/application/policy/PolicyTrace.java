package com.soul.fin.common.command.application.policy;

import java.time.Duration;

public record PolicyTrace(
        String policyName,
        PolicyDecision decision,
        Duration executionTime
) {
}

