package com.soul.fin.common.bus.core;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface QueryHandler<Q extends Query<R>, R> {

    Mono<R> handle(Q query);

}