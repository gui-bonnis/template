package com.soul.fin.common.bus.middleware;

import com.soul.fin.common.bus.core.Query;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface NextQuery {
    <R, Q extends Query<R>> Mono<Q> invoke(Q qry);
}
