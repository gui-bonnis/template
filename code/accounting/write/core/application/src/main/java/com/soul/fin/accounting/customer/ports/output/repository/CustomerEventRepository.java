package com.soul.fin.accounting.customer.ports.output.repository;

import com.soul.fin.accounting.customer.vo.CustomerId;
import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.application.ports.output.repository.AggregateEventRepository;
import com.soul.fin.common.core.event.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerEventRepository extends AggregateEventRepository<CustomerId> {

    Flux<DomainEvent> load(CustomerId customerId);

    Mono<EventEnvelope> append(EventEnvelope eventEnvelope);

}
