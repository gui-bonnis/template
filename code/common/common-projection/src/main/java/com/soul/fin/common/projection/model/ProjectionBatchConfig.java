package com.soul.fin.common.projection.model;

import java.time.Duration;

public record ProjectionBatchConfig(
        int maxEvents,
        Duration maxWait
) {
    public static ProjectionBatchConfig defaultConfig() {
        return new ProjectionBatchConfig(100, Duration.ofMillis(500));
    }
}

