package com.soul.fin.accounting.write.data.customer.adapter;


import com.soul.fin.accounting.write.customer.ports.output.repository.AccountingSnapshotRepository;
import com.soul.fin.common.application.dto.SnapshotEnvelope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class SnapshotRepositoryImpl implements AccountingSnapshotRepository {
    @Override
    public Mono<SnapshotEnvelope> load(UUID aggregateId) {
        return null;
    }

    @Override
    public Mono<Void> save(SnapshotEnvelope snapshot) {
        return null;
    }

}