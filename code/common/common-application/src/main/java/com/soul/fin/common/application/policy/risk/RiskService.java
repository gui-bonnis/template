package com.soul.fin.common.application.policy.risk;

public interface RiskService {

    RiskScore evaluate(Object input);

}

