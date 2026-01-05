package com.soul.fin.accounting.write.customer.ports.output.repository;

import com.soul.fin.common.application.dto.SnapshotEnvelope;
import com.soul.fin.common.application.ports.output.repository.SnapshotRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AccountingSnapshotRepository extends SnapshotRepository<UUID> {
    Mono<SnapshotEnvelope> load(UUID aggregateId);

    Mono<Void> save(SnapshotEnvelope snapshot);
}
