package com.soul.fin.accounting.projection.data.poison;

import com.soul.fin.common.projection.model.EventEnvelope;
import com.soul.fin.common.projection.ports.PoisonEventRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
class PostgresPoisonEventRepository implements PoisonEventRepository {

    private final DatabaseClient db;

    PostgresPoisonEventRepository(DatabaseClient db) {
        this.db = db;
    }

    @Override
    public Mono<Void> save(
            String projectionName,
            EventEnvelope event,
            Throwable error
    ) {
        return db.sql("""
                            INSERT INTO accounting_projections.poison_event (
                                projection_name,
                                global_position,
                                event_type,
                                schema_version,
                                payload,
                                metadata,
                                error_message,
                                stack_trace,
                                quarantined_at
                            )
                            VALUES (:projection, :pos, :type, :version, :payload, :metadata, :msg, :stack, now())
                        """)
                .bind("projection", projectionName)
                .bind("pos", event.eventPosition())
                .bind("type", event.eventType())
                .bind("version", event.eventSchemaVersion())
                .bind("payload", event.payload().toString())
                .bind("metadata", event.metadata().toString())
                .bind("msg", error.getMessage())
                .bind("stack", error.getStackTrace().toString())
                .then();
    }
}

