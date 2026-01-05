package com.soul.fin.accounting.write.customer.ports.output.repository;

import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.application.ports.output.repository.EventRepository;
import com.soul.fin.common.core.event.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AccountingEventRepository extends EventRepository<UUID> {

    Flux<DomainEvent> load(UUID aggregateId);

    Mono<EventEnvelope> append(EventEnvelope eventEnvelope);

}
