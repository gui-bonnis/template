package com.soul.fin.accounting.read.customer.ports.output.repository;

import com.soul.fin.accounting.read.customer.entity.CustomerSummary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CustomerSummaryRepository {
    Mono<CustomerSummary> findById(UUID id);

    Flux<CustomerSummary> findAll();

    Flux<CustomerSummary> findBy(int page, int size);

    Mono<Boolean> existsById(UUID id);

}
