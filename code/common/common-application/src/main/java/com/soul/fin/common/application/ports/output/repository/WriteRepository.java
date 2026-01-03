package com.soul.fin.common.application.ports.output.repository;

import com.soul.fin.common.core.entity.BaseAggregateRoot;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface WriteRepository<A extends BaseAggregateRoot<?>> {

    Mono<A> save(A aggregate);

    Mono<A> insert(A aggregate);

    Mono<A> update(A aggregate);
    
    Mono<Void> deleteById(UUID id);

}
