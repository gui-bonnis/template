package com.soul.fin.accounting.read.data.customer.repository;

import com.soul.fin.accounting.read.data.customer.entity.CustomerEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface CustomerReactiveRepository extends ReactiveCrudRepository<CustomerEntity, UUID> {

    Flux<CustomerEntity> findBy(Pageable pageable);

}
