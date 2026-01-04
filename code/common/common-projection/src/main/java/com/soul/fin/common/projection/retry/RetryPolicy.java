package com.soul.fin.common.projection.retry;

import java.time.Duration;

public record RetryPolicy(
        int maxAttempts,
        Duration initialBackoff,
        Duration maxBackoff,
        double jitter
) {
    public static RetryPolicy defaultPolicy() {
        return new RetryPolicy(
                5,
                Duration.ofMillis(200),
                Duration.ofSeconds(5),
                0.5
        );
    }
}

