package com.soul.fin.accounting.data.customer.repository;

import com.soul.fin.accounting.data.customer.entity.CustomerEventEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface CustomerEventReactiveRepository extends ReactiveCrudRepository<CustomerEventEntity, Long> {

    Flux<CustomerEventEntity> findByAggregateIdOrderByAggregateVersion(UUID aggregateId);
}

