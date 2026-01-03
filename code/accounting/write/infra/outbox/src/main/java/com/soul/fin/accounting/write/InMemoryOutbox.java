package com.soul.fin.accounting;

import com.soul.fin.accounting.common.OutboxMessage;
import com.soul.fin.accounting.common.OutboxReader;
import com.soul.fin.accounting.common.OutboxStatus;
import com.soul.fin.accounting.common.OutboxWriter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemoryOutbox implements OutboxReader, OutboxWriter {

    private final Map<UUID, OutboxMessage> store = new LinkedHashMap<>();

    @Override
    public void save(OutboxMessage message) {
        store.put(message.id(), message);
    }

    @Override
    public List<OutboxMessage> findPending(int batchSize) {
        return store.values().stream()
                .filter(m -> m.status() == OutboxStatus.PENDING)
                .limit(batchSize)
                .toList();
    }

    @Override
    public void markSent(UUID messageId) {
        update(messageId, OutboxStatus.SENT);
    }

    @Override
    public void markFailed(UUID messageId, String reason) {
        update(messageId, OutboxStatus.FAILED);
    }

    private void update(UUID id, OutboxStatus status) {
        store.computeIfPresent(
                id,
                (k, msg) -> new OutboxMessage(
                        msg.id(),
                        msg.type(),
                        msg.payload(),
                        msg.metadata(),
                        msg.occurredAt(),
                        status
                )
        );
    }
}
