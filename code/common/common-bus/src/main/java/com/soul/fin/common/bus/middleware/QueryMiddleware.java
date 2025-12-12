package com.soul.fin.common.bus.middleware;

import com.soul.fin.common.bus.core.Query;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@FunctionalInterface
public interface QueryMiddleware {
    <R, Q extends Query<R>> Function<Q, Mono<R>> apply(Function<Q, Mono<R>> next);
}
