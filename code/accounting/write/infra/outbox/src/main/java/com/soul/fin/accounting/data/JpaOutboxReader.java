package com.soul.fin.accounting.data;

import com.soul.fin.accounting.common.OutboxMessage;
import com.soul.fin.accounting.common.OutboxReader;
import com.soul.fin.accounting.common.OutboxStatus;

import java.util.List;
import java.util.UUID;

public class JpaOutboxReader implements OutboxReader {

    private final JpaOutboxRepository repository;

    public JpaOutboxReader(JpaOutboxRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<OutboxMessage> findPending(int batchSize) {
        return repository.findPending(batchSize)
                .stream()
                .map(JpaOutboxEntity::toModel)
                .toList();
    }

    @Override
    public void markSent(UUID messageId) {
        repository.updateStatus(messageId, OutboxStatus.SENT);
    }

    @Override
    public void markFailed(UUID messageId, String reason) {
        repository.updateStatus(messageId, OutboxStatus.FAILED);
    }
}
