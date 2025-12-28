package com.soul.fin.common.application;

import com.soul.fin.common.core.event.DomainEvent;
import reactor.core.publisher.Flux;

public interface AggregateFactory<T> {

    T createEmpty();

    T rehydrate(Flux<DomainEvent> events);
}
