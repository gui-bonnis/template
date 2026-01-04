package com.soul.fin.accounting.write.data.events.repository;

import com.soul.fin.accounting.write.data.events.entity.EventsEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface EventsReactiveRepository extends ReactiveCrudRepository<EventsEntity, Long> {

    Flux<EventsEntity> findByAggregateIdOrderByAggregateVersion(UUID aggregateId);
}

