package com.soul.fin.common.bus.core;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QueryBus {

    <R, Q extends QueryOne<R>> Mono<R> ask(Q query);

    <R, Q extends QueryMany<R>> Flux<R> askMany(Q query);
}
