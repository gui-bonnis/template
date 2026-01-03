package com.soul.fin.common.bus.core.command;

public sealed interface RetryPolicy {

    static RetryPolicy none() {
        return new None();
    }

    static RetryPolicy optimistic(int maxRetries) {
        return new Optimistic(maxRetries);
    }

    record None() implements RetryPolicy {
    }

    record Optimistic(int maxRetries) implements RetryPolicy {
    }
}

