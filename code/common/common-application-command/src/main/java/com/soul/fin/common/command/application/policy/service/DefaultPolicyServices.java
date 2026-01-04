package com.soul.fin.common.command.application.policy.service;

import com.soul.fin.common.command.application.policy.feature.FeatureFlags;
import com.soul.fin.common.command.application.policy.risk.RiskService;

import java.time.Clock;
import java.util.Map;

public final class DefaultPolicyServices implements PolicyServices {

    private Map<Class<?>, Object> readModels;
    private final FeatureFlags featureFlags;
    private final RiskService riskService;
    private final Clock clock;

    public DefaultPolicyServices(
            Map<Class<?>, Object> readModels,
            FeatureFlags featureFlags,
            RiskService riskService,
            Clock clock
    ) {
        this.readModels = readModels;
        this.featureFlags = featureFlags;
        this.riskService = riskService;
        this.clock = clock;
    }

    public DefaultPolicyServices(
            //Map<Class<?>, Object> readModels,
            FeatureFlags featureFlags,
            RiskService riskService,
            Clock clock
    ) {
        this.featureFlags = featureFlags;
        this.riskService = riskService;
        this.clock = clock;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readModel(Class<T> type) {
        return (T) readModels.get(type);
    }

    @Override
    public FeatureFlags featureFlags() {
        return featureFlags;
    }

    @Override
    public RiskService riskService() {
        return riskService;
    }

    @Override
    public Clock clock() {
        return clock;
    }
}

