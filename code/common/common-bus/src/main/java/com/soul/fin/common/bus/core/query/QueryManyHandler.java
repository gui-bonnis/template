package com.soul.fin.common.bus.core.query;

import reactor.core.publisher.Flux;

@FunctionalInterface
public interface QueryManyHandler<Q extends QueryMany<R>, R> {
    Flux<R> handle(Q query);
}