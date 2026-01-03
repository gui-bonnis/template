package com.soul.fin.common.application.ports.output.repository;

import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.core.event.DomainEvent;
import com.soul.fin.common.core.vo.BaseId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventRepository<T extends BaseId<?>> {
    Flux<DomainEvent> load(T t);

    Mono<EventEnvelope> append(EventEnvelope eventEnvelope);
}
