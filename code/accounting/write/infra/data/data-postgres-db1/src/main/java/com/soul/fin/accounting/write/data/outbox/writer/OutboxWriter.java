package com.soul.fin.accounting.write.data.outbox.writer;

import com.soul.fin.accounting.write.data.outbox.entity.OutboxEntity;
import reactor.core.publisher.Mono;

public interface OutboxWriter {
    Mono<Void> append(OutboxEntity entity);
}
