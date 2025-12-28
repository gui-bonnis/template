package com.soul.fin.accounting.customer.repository;

import com.soul.fin.accounting.customer.entity.CustomerEventEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface CustomerJPAEventStoreRepository
        extends ReactiveCrudRepository<CustomerEventEntity, Long> {

    Flux<CustomerEventEntity> findByCustomerIdOrderByAggregateVersion(UUID customerId);
}

