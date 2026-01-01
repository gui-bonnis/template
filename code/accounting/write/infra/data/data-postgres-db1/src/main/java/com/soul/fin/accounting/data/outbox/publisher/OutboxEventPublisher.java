package com.soul.fin.accounting.data.outbox.publisher;

import com.soul.fin.accounting.data.outbox.entity.OutboxEntity;
import com.soul.fin.accounting.data.outbox.repository.OutboxRepository;
import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.application.ports.output.publisher.EventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@org.springframework.stereotype.Component
public class OutboxEventPublisher implements EventPublisher {

    private final OutboxRepository outboxRepository;

    public OutboxEventPublisher(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

//    @Override
//    public Mono<Void> publish(Flux<EventEnvelope> events) {
//        return events
//                .map(eventEnvelope -> {
//                    return new OutboxEntity(
//                            eventEnvelope.aggregateId(),
//                            eventEnvelope.eventId(),
//                            eventEnvelope.eventType(),
//                            eventEnvelope.payload().toString(),
//                            eventEnvelope.metadata().toString(),
//                            Instant.now(),
//                            null,
//                            "PENDENT"
//                    );
//                })
//                .flatMap(outboxRepository::save)
//                .then();
//
//    }

    @Override
    public Mono<Void> publish(List<EventEnvelope> events) {
        return Flux.fromIterable(events)
                .map(eventEnvelope -> {
                    return new OutboxEntity(
                            eventEnvelope.aggregateId(),
                            eventEnvelope.eventId(),
                            eventEnvelope.eventType(),
                            eventEnvelope.payload().toString(),
                            eventEnvelope.metadata().toString(),
                            Instant.now(),
                            null,
                            "PENDENT"
                    );
                })
                .flatMap(outboxRepository::save)
                .then();

    }

}
