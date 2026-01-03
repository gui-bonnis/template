package com.soul.fin.accounting.write.data.customer.writer;

import com.soul.fin.accounting.write.data.customer.entity.CustomerEventEntity;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class PostgresEventStoreWriter implements EventStoreWriter {

    private final DatabaseClient client;

    PostgresEventStoreWriter(DatabaseClient client) {
        this.client = client;
    }

    @Override
    public Mono<Long> append(CustomerEventEntity entity) {
        return client.sql("""
                            INSERT INTO customer.event_store_customer (
                                aggregate_id,
                                aggregate_type,
                                aggregate_version,
                                event_id,
                                event_type,
                                event_schema_version,
                                payload,
                                metadata
                            )
                            VALUES (
                                :aggregateId,
                                :aggregateType,
                                :aggregateVersion,
                                :eventId,
                                :eventType,
                                :eventSchemaVersion,
                                CAST(:payload AS jsonb),
                                CAST(:metadata AS jsonb)
                            )
                            RETURNING event_position6
                        """)
                .bind("aggregateId", entity.getAggregateId())
                .bind("aggregateType", entity.getAggregateType())
                .bind("aggregateVersion", entity.getAggregateVersion())
                .bind("eventId", entity.getEventId())
                .bind("eventType", entity.getEventType())
                .bind("eventSchemaVersion", entity.getEventSchemaVersion())
                .bind("payload", entity.getPayload())
                .bind("metadata", entity.getMetadata())
                .map(row -> Objects.requireNonNull(row.get("event_position", Long.class)))
                .one();
    }
}

