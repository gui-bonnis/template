package com.soul.fin.accounting.write.data.customer.writer;

import com.soul.fin.accounting.write.data.customer.entity.CustomerEventEntity;
import reactor.core.publisher.Mono;

public interface EventStoreWriter {
    Mono<Long> append(CustomerEventEntity entity);
}
