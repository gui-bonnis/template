package com.soul.fin.accounting.data.customer.writer;

import com.soul.fin.accounting.data.customer.entity.CustomerEventEntity;
import reactor.core.publisher.Mono;

public interface EventStoreWriter {
    Mono<Void> append(CustomerEventEntity entity);
}
