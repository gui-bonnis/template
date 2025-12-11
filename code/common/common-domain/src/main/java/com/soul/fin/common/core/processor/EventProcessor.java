package com.soul.fin.common.core.processor;

import com.soul.fin.common.core.event.DomainEvent;

public interface EventProcessor<T extends DomainEvent, R extends DomainEvent> {

    R process(T event);
    //Mono<R> process(T event);

}