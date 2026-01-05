package com.soul.fin.common.projection.api;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

public interface ProjectionAckAwaiter {

    Mono<Void> await(
            UUID eventId,
            String projectionName,
            Duration timeout
    );
}

