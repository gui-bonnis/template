package com.soul.fin.accounting.data.outbox.repository;

import com.soul.fin.accounting.data.outbox.entity.OutboxEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OutboxRepository extends ReactiveCrudRepository<OutboxEntity, UUID> {
}
