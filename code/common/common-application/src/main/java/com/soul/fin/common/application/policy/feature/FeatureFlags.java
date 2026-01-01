package com.soul.fin.common.application.policy.feature;

public interface FeatureFlags {

    boolean isEnabled(String featureKey);

}

