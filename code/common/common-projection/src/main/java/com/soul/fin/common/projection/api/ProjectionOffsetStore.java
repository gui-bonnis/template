package com.soul.fin.common.projection.api;

import reactor.core.publisher.Mono;

public interface ProjectionOffsetStore {

    Mono<Long> loadOffset(String projectionName);

    Mono<Void> saveOffset(String projectionName, long eventPosition);
}

