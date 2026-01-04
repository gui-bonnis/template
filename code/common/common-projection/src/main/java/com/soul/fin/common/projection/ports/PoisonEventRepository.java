package com.soul.fin.common.projection.ports;

import com.soul.fin.common.projection.model.EventEnvelope;
import reactor.core.publisher.Mono;

public interface PoisonEventRepository {

    Mono<Void> save(
            String projectionName,
            EventEnvelope event,
            Throwable error
    );
}

