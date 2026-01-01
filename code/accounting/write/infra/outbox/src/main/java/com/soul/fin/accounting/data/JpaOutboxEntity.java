package com.soul.fin.accounting.data;

import com.soul.fin.accounting.common.OutboxMessage;
import com.soul.fin.accounting.common.OutboxStatus;

import java.time.Instant;
import java.util.UUID;

public class JpaOutboxEntity {

    public UUID id;
    public String type;
    public String payload;
    public String metadata;
    public Instant occurredAt;
    public OutboxStatus status;

    public static JpaOutboxEntity from(OutboxMessage msg) {
        JpaOutboxEntity e = new JpaOutboxEntity();
        e.id = msg.id();
        e.type = msg.type();
        e.payload = msg.payload();
        e.metadata = msg.metadata();
        e.occurredAt = msg.occurredAt();
        e.status = msg.status();
        return e;
    }

    public OutboxMessage toModel() {
        return new OutboxMessage(
                id,
                type,
                payload,
                metadata,
                occurredAt,
                status
        );
    }
}
