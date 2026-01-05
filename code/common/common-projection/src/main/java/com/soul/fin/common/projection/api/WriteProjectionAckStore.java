package com.soul.fin.common.projection.api;

import com.soul.fin.common.projection.model.ProjectionAck;
import reactor.core.publisher.Mono;

public interface WriteProjectionAckStore {

    Mono<Void> record(ProjectionAck ack);

}

