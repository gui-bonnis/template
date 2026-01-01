package com.soul.fin.accounting.data.outbox.writer;

import com.soul.fin.accounting.data.outbox.entity.OutboxEntity;
import reactor.core.publisher.Mono;

public interface OutboxWriter {
    Mono<Void> append(OutboxEntity entity);
}
