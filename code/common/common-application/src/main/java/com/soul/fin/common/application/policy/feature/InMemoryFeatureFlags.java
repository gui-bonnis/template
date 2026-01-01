package com.soul.fin.common.application.policy.feature;

import java.util.Set;

public final class InMemoryFeatureFlags implements FeatureFlags {

    private final Set<String> enabled;

    public InMemoryFeatureFlags(Set<String> enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled(String featureKey) {
        return enabled.contains(featureKey);
    }
}

