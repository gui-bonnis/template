package com.soul.fin.accounting.projection.customer.repository;

import com.soul.fin.accounting.read.customer.entity.CustomerSummaryV1;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

public interface CustomerSummaryRepositoryV1 {


    Mono<Void> insert(CustomerSummaryV1 view);

    Mono<Void> increaseAvailable(
            UUID customerId,
            String name,
            long version,
            Instant at
    );
}
