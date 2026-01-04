package com.soul.fin.common.command.application.policy.feature;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
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

