package com.soul.fin.accounting.projection.data.offset;

import com.soul.fin.common.projection.api.ProjectionOffsetStore;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
class PostgresProjectionOffsetStore implements ProjectionOffsetStore {

    private final DatabaseClient db;

    PostgresProjectionOffsetStore(DatabaseClient db) {
        this.db = db;
    }

    @Override
    public Mono<Long> loadOffset(String projectionName) {
        return db.sql("""
                            SELECT last_position
                            FROM accounting_projections.projection_offset
                            WHERE projection_name = :name
                        """)
                .bind("name", projectionName)
                .map(row -> Objects.requireNonNull(row.get("last_position", Long.class)))
                .one();
    }

    @Override
    public Mono<Void> saveOffset(String projectionName, long position) {
        return db.sql("""
                            INSERT INTO accounting_projections.projection_offset (projection_name, last_position)
                            VALUES (:name, :pos)
                            ON CONFLICT (projection_name)
                            DO UPDATE SET last_position = EXCLUDED.last_position
                        """)
                .bind("name", projectionName)
                .bind("pos", position)
                .then();
    }
}

