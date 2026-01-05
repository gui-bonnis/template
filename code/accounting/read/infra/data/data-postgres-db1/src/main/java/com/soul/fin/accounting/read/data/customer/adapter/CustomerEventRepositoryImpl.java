package com.soul.fin.accounting.read.data.customer.adapter;

import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.application.ports.output.repository.EventRepository;
import com.soul.fin.common.core.event.DomainEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CustomerEventRepositoryImpl implements EventRepository {


    @Override
    public Flux<DomainEvent> load(UUID baseId) {
        return null;
    }

    @Override
    public Mono<EventEnvelope> append(EventEnvelope eventEnvelope) {
        return null;
    }
}