package com.soul.fin.common.projection.dsl;

import com.soul.fin.common.projection.model.EventEnvelope;
import reactor.core.publisher.Mono;

public interface TypedEventHandler<T> {
    Mono<Void> handle(T payload, EventEnvelope envelope);
}

