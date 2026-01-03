package com.soul.fin.common.bus.core.query;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface QueryOneHandler<Q extends QueryOne<R>, R> {
    Mono<R> handle(Q query);
}
