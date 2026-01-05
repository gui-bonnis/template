package com.soul.fin.accounting.projection.data.ack;

import com.soul.fin.common.projection.api.WriteProjectionAckStore;
import com.soul.fin.common.projection.model.ProjectionAck;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
class PostgresWriteProjectionAckStore implements WriteProjectionAckStore {

    private final DatabaseClient db;

    PostgresWriteProjectionAckStore(DatabaseClient db) {
        this.db = db;
    }

    @Override
    public Mono<Void> record(ProjectionAck ack) {
        return db.sql("""
                            INSERT INTO accounting_projections.projection_ack (
                            event_id, projection_name, processed_at)
                            VALUES (:id, :name, :processedAt)
                            ON CONFLICT (event_id, projection_name)
                            DO UPDATE SET processed_at = :processedAt
                        """)
                .bind("id", ack.eventId())
                .bind("name", ack.projectionName())
                .bind("processedAt", ack.processedAt())
                .then();
    }


}

