package com.soul.fin.accounting.write.customer.policy.service;

import com.soul.fin.common.command.application.policy.risk.RiskLevel;
import com.soul.fin.common.command.application.policy.risk.RiskScore;
import com.soul.fin.common.command.application.policy.risk.RiskService;
import org.springframework.stereotype.Component;

@Component
public final class StubRiskService implements RiskService {

    @Override
    public RiskScore evaluate(Object input) {
        return new RiskScore(0, RiskLevel.HIGH);
    }
}

