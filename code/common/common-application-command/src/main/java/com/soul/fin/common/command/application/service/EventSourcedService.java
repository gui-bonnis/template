package com.soul.fin.common.command.application.service;

import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.application.mapper.AggregateFactory;
import com.soul.fin.common.application.ports.output.repository.EventRepository;
import com.soul.fin.common.core.entity.BaseAggregateRoot;
import com.soul.fin.common.core.vo.BaseId;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class EventSourcedService<T extends BaseId<?>, A extends BaseAggregateRoot<T>> {

    private final EventRepository<T> eventRepository;
    private final AggregateFactory<A> factory;

    public EventSourcedService(EventRepository<T> eventRepository, AggregateFactory<A> factory) {
        this.eventRepository = eventRepository;
        this.factory = factory;
    }

    public Mono<A> load(T id) {
        return Mono.just(factory
                .rehydrate(eventRepository.load(id)));
    }

    public Flux<EventEnvelope> save(List<EventEnvelope> eventEnvelopes) {
        return Flux.fromIterable(eventEnvelopes)
                .flatMap(eventRepository::append);
    }

}

