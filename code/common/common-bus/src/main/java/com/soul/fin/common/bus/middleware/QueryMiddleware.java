package com.soul.fin.common.bus.middleware;

import com.soul.fin.common.bus.core.QueryMany;
import com.soul.fin.common.bus.core.QueryOne;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface QueryMiddleware {
    <R, Q extends QueryOne<R>> Function<Q, Mono<R>> applyOne(Function<Q, Mono<R>> next);

    <R, Q extends QueryMany<R>> Function<Q, Flux<R>> applyMany(Function<Q, Flux<R>> next);
}
