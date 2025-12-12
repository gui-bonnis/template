package com.soul.fin.common.bus.middleware;

import com.soul.fin.common.bus.core.Command;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@FunctionalInterface
public interface CommandMiddleware {
    <R, C extends Command<R>> Function<C, Mono<R>> apply(Function<C, Mono<R>> next);
}
