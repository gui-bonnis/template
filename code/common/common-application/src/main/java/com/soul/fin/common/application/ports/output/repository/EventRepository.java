package com.soul.fin.common.application.ports.output.repository;

import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.core.event.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EventRepository<T extends UUID> {
    Flux<DomainEvent> load(T t);

    Mono<EventEnvelope> append(EventEnvelope eventEnvelope);
}
