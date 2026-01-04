package com.soul.fin.accounting.write.data.customer.writer;

import com.soul.fin.accounting.write.data.customer.entity.CustomerEventEntity;
import com.soul.fin.common.core.exception.OptimisticConcurrencyException;
import org.springframework.dao.DuplicateKeyException;
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
                            INSERT INTO accounting_events_store.customer (
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
                            RETURNING global_position
                        """)
                .bind("aggregateId", entity.getAggregateId())
                .bind("aggregateType", entity.getAggregateType())
                .bind("aggregateVersion", entity.getAggregateVersion())
                .bind("eventId", entity.getEventId())
                .bind("eventType", entity.getEventType())
                .bind("eventSchemaVersion", entity.getEventSchemaVersion())
                .bind("payload", entity.getPayload())
                .bind("metadata", entity.getMetadata())
                .map(row -> Objects.requireNonNull(row.get("global_position", Long.class)))
                .one()
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> new OptimisticConcurrencyException(
                                entity.getAggregateId(),
                                entity.getAggregateVersion(),
                                ex
                        )
                );
    }
}

