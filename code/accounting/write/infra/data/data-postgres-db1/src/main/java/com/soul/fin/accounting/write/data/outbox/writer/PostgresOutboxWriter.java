package com.soul.fin.accounting.write.data.outbox.writer;

import com.soul.fin.accounting.write.data.outbox.entity.OutboxEntity;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PostgresOutboxWriter implements OutboxWriter {

    private final DatabaseClient client;

    PostgresOutboxWriter(DatabaseClient client) {
        this.client = client;
    }

    @Override
    public Mono<Void> append(OutboxEntity entity) {
        return client.sql("""
                            INSERT INTO outbox.customer (
                                aggregate_id,
                                event_id,
                                event_position,
                                type,
                                payload,
                                metadata,
                                created_at,
                                status
                            )
                            VALUES (
                                :aggregateId,
                                :eventId,
                                :eventPosition,
                                :type,
                                CAST(:payload AS jsonb),
                                CAST(:metadata AS jsonb),
                                :createdAt,
                                :status
                            )
                        """)
                .bind("aggregateId", entity.getAggregateId())
                .bind("eventId", entity.getEventId())
                .bind("eventPosition", entity.getEventPosition())
                .bind("type", entity.getType())
                .bind("payload", entity.getPayload())
                .bind("metadata", entity.getMetadata())
                .bind("createdAt", entity.getCreatedAt())
                .bind("status", entity.getStatus())
                .then();
    }
}

