package com.soul.fin.common.bus.core;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface CommandHandler<C extends Command<R>, R> {
    Mono<R> handle(C command);
}