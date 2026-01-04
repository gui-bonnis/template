package com.soul.fin.accounting.write.data.events.writer;

import com.soul.fin.accounting.write.data.events.entity.EventsEntity;
import reactor.core.publisher.Mono;

public interface EventStoreWriter {
    Mono<Long> append(EventsEntity entity);
}
