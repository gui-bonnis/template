package com.soul.fin.common.application.event;

import com.soul.fin.common.application.dto.EventEnvelope;
import reactor.core.publisher.Mono;

import java.util.List;

public interface EventMiddleware {

    Mono<Void> handle(
            List<EventEnvelope> events,
            EventMiddlewareChain next
    );
}

