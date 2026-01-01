package com.soul.fin.common.application.service;

import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.application.mapper.AggregateFactory;
import com.soul.fin.common.application.ports.output.repository.AggregateEventRepository;
import com.soul.fin.common.core.entity.BaseAggregateRoot;
import com.soul.fin.common.core.vo.BaseId;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class EventSourcedService<T extends BaseId<?>, A extends BaseAggregateRoot<T>> {

    private final AggregateEventRepository<T> eventRepository;
    private final AggregateFactory<A> factory;

    public EventSourcedService(AggregateEventRepository<T> eventRepository, AggregateFactory<A> factory) {
        this.eventRepository = eventRepository;
        this.factory = factory;
    }

    public Mono<A> load(T id) {
        return Mono.just(factory
                .rehydrate(eventRepository.load(id)));
    }

//    public Mono<Void> save(Flux<EventEnvelope> eventEnvelopes) {
//        return eventEnvelopes
//                .concatMap(eventRepository::append)  // Process each envelope sequentially, calling append(EventEnvelope) -> Mono<Void>
//                .then();  // Convert to Mono<Void> after all are processed
//    }

    public Mono<Void> save(List<EventEnvelope> eventEnvelopes) {
        return Flux.fromIterable(eventEnvelopes)
                .map(eventRepository::append)// not sure if they are processed sequentially
                .then();  // Convert to Mono<Void> after all are processed
    }

}

