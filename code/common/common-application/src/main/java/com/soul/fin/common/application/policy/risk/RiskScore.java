package com.soul.fin.common.application.policy.risk;

public record RiskScore(
        int value,
        RiskLevel level
) {
    public boolean isHigh() {
        return level == RiskLevel.HIGH;
    }
}

