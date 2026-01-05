package com.soul.fin.common.command.application.service;

import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.application.mapper.AggregateFactory;
import com.soul.fin.common.application.ports.output.repository.EventRepository;
import com.soul.fin.common.application.ports.output.repository.SnapshotRepository;
import com.soul.fin.common.core.entity.BaseAggregateRoot;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
public class EventSourcedService<T extends UUID, A extends BaseAggregateRoot<?>> {

    private final EventRepository<T> eventRepository;
    private final SnapshotRepository<T> snapshotRepository;
    private final AggregateFactory<A> factory;

    public EventSourcedService(EventRepository<T> eventRepository, SnapshotRepository<T> snapshotRepository, AggregateFactory<A> factory) {
        this.eventRepository = eventRepository;
        this.snapshotRepository = snapshotRepository;
        this.factory = factory;
    }

    public Mono<A> load(T id) {
        return Mono.just(factory
                .rehydrate(eventRepository.load(id)));
//
//        return snapshotRepository.load(id)
//                .flatMap(snapshot -> {
//                    A aggregate = factory.fromSnapshot(snapshot);
//                    aggregate.restoreVersion(snapshot.aggregateVersion());
//
//                    return eventRepository
//                            .loadAfter(snapshot.globalPosition())
//                            .doOnNext(aggregate::apply)
//                            .thenReturn(aggregate);
//                })
//                .switchIfEmpty(
//                        eventRepository.load(id)
//                                .reduce(factory.empty(), (agg, event) -> {
//                                    agg.apply(event);
//                                    return agg;
//                                })
//                );
    }

    public Flux<EventEnvelope> save(List<EventEnvelope> eventEnvelopes) {
        return Flux.fromIterable(eventEnvelopes)
                .flatMap(eventRepository::append);
    }

}

