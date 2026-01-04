package com.soul.fin.common.projection.api;

import com.soul.fin.common.projection.model.EventEnvelope;
import reactor.core.publisher.Flux;

public interface EventStream {

    Flux<EventEnvelope> streamFrom(long eventPositionExclusive);
}

