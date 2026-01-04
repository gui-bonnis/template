package com.soul.fin.accounting.projection.data.customer.event;

import com.soul.fin.accounting.projection.data.util.EventEnvelopeRowMapper;
import com.soul.fin.common.projection.api.EventStream;
import com.soul.fin.common.projection.model.EventEnvelope;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
class PostgresEventStream implements EventStream {

    private final DatabaseClient db;

    PostgresEventStream(DatabaseClient db) {
        this.db = db;
    }

    @Override
    public Flux<EventEnvelope> streamFrom(long positionExclusive) {
        return db.sql("""
                            SELECT *
                            FROM accounting_events_store.customer
                            WHERE global_position > :pos
                            ORDER BY global_position
                        """)
                .bind("pos", positionExclusive)
                .map(EventEnvelopeRowMapper::map)
                .all();
    }
}

