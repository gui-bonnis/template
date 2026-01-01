package com.soul.fin.common.application.policy.service;

import com.soul.fin.common.application.policy.feature.FeatureFlags;
import com.soul.fin.common.application.policy.risk.RiskService;

import java.time.Clock;

public interface PolicyServices {

    <T> T readModel(Class<T> type);

    Clock clock();

    FeatureFlags featureFlags();

    RiskService riskService();
}

