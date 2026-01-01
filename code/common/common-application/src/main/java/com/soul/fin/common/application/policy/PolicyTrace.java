package com.soul.fin.common.application.policy;

import java.time.Duration;

public record PolicyTrace(
        String policyName,
        PolicyDecision decision,
        Duration executionTime
) {
}

