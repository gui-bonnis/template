package com.soul.fin.common.bus.middleware;

import com.soul.fin.common.bus.core.Command;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface CommandMiddleware {
    <R, C extends Command<R>> Mono<C> invoke(C command, NextCommand next);
}
