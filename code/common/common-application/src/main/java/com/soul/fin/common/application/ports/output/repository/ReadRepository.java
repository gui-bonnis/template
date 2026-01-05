package com.soul.fin.common.application.ports.output.repository;


import com.soul.fin.common.core.entity.BaseAggregateRoot;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReadRepository<A extends BaseAggregateRoot<?>> {
    Mono<A> findById(UUID id);

    Flux<A> findAll();

    Flux<A> findBy(int page, int size);

    Mono<Boolean> existsById(UUID id);
}
