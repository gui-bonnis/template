package com.soul.fin.accounting.outbox;

import com.soul.fin.accounting.common.MessagePublisher;
import com.soul.fin.accounting.common.OutboxMessage;
import com.soul.fin.accounting.common.OutboxReader;

import java.util.List;

public class OutboxDispatcher {

    private final OutboxReader reader;
    private final MessagePublisher publisher;
    private final int batchSize;

    public OutboxDispatcher(
            OutboxReader reader,
            MessagePublisher publisher,
            int batchSize
    ) {
        this.reader = reader;
        this.publisher = publisher;
        this.batchSize = batchSize;
    }

    public void dispatchOnce() {
        List<OutboxMessage> messages = reader.findPending(batchSize);

        for (OutboxMessage message : messages) {
            try {
                publisher.publish(message);
                reader.markSent(message.id());
            } catch (Exception ex) {
                reader.markFailed(message.id(), ex.getMessage());
            }
        }
    }
}
