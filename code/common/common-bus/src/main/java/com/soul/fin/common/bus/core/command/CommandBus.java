package com.soul.fin.common.bus.core.command;

import reactor.core.publisher.Mono;

public interface CommandBus {
    <R, C extends Command<R>> Mono<R> execute(C command);
}
