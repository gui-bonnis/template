package com.soul.fin.common.application.ports.output.repository;

import com.soul.fin.common.application.dto.SnapshotEnvelope;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SnapshotRepository<T extends UUID> {

    Mono<SnapshotEnvelope> load(UUID aggregateId);

    Mono<Void> save(SnapshotEnvelope snapshot);
}
