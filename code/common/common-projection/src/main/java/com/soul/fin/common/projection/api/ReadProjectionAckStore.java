package com.soul.fin.common.projection.api;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReadProjectionAckStore {

    Mono<Boolean> exists(UUID eventId, String projectionName);
}

