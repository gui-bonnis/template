package com.soul.fin.accounting.write.data.events.ack;

import com.soul.fin.common.projection.api.ReadProjectionAckStore;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@Component
class PostgresReadProjectionAckStore implements ReadProjectionAckStore {

    private final DatabaseClient db;

    PostgresReadProjectionAckStore(DatabaseClient db) {
        this.db = db;
    }

    @Override
    public Mono<Boolean> exists(UUID eventId, String projectionName) {
        return db.sql("""
                            SELECT 1 as found
                            FROM accounting_projections.projection_ack
                            WHERE event_id = :eventId
                              AND projection_name = :name
                        """)
                .bind("eventId", eventId)
                .bind("name", projectionName)
                .map(row -> Objects.requireNonNull(row.get("found", Integer.class)))
                .one()
                .hasElement();
    }


}

