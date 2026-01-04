package com.soul.fin.common.command.application.event.middleware;

import com.soul.fin.common.application.dto.EventEnvelope;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IntegrationEventDispatcher {
    Mono<Void> dispatch(List<EventEnvelope> events);
}

