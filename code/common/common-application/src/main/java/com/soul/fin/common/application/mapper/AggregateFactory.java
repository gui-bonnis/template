package com.soul.fin.common.application.mapper;

import com.soul.fin.common.application.dto.SnapshotEnvelope;
import com.soul.fin.common.core.event.DomainEvent;
import reactor.core.publisher.Flux;

public interface AggregateFactory<T> {

    T createEmpty();

    T rehydrate(Flux<DomainEvent> events);

    T fromSnapshot(SnapshotEnvelope snapshot);
}
