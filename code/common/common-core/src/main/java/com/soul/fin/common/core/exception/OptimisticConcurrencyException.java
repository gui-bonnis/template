package com.soul.fin.common.core.exception;

import java.util.UUID;

public class OptimisticConcurrencyException extends RuntimeException {

    private final UUID aggregateId;
    private final long expectedVersion;

    public OptimisticConcurrencyException(
            UUID aggregateId,
            long expectedVersion,
            Throwable cause
    ) {
        super(
                "Optimistic concurrency conflict on aggregate " + aggregateId +
                        " at expected version " + expectedVersion,
                cause
        );
        this.aggregateId = aggregateId;
        this.expectedVersion = expectedVersion;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public long getExpectedVersion() {
        return expectedVersion;
    }
}

