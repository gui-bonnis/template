package com.soul.fin.common.projection.engine;

import reactor.core.publisher.Mono;

public interface TransactionManager {

    <T> Mono<T> inTransaction(Mono<T> work);
}

