package com.soul.fin.common.bus.core.command;

public interface Command<R> {
    CommandMetadata metadata();

    default RetryPolicy retryPolicy() {
        return RetryPolicy.none();
    }
}