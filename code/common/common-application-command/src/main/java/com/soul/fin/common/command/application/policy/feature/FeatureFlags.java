package com.soul.fin.common.command.application.policy.feature;

public interface FeatureFlags {

    boolean isEnabled(String featureKey);

}

