package com.soul.fin.accounting.common;

import java.util.List;
import java.util.UUID;

public interface OutboxReader {

    List<OutboxMessage> findPending(int batchSize);

    void markSent(UUID messageId);

    void markFailed(UUID messageId, String reason);
}