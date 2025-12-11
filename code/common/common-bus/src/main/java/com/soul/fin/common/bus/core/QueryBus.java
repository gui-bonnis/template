package com.soul.fin.common.bus.core;

import reactor.core.publisher.Mono;

public interface QueryBus {
    <R, Q extends Query<R>> Mono<Q> execute(Q query);
}
