package com.soul.fin.common.command.application.policy.risk;

public record RiskScore(
        int value,
        RiskLevel level
) {
    public boolean isHigh() {
        return level == RiskLevel.HIGH;
    }
}

