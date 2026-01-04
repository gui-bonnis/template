package com.soul.fin.common.command.application.policy.risk;

public interface RiskService {

    RiskScore evaluate(Object input);

}

