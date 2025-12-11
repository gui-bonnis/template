package com.soul.fin.common.bus.core;

import reactor.core.publisher.Mono;

public interface CommandBus {
    <R, C extends Command<R>> Mono<C> execute(C command);
}
