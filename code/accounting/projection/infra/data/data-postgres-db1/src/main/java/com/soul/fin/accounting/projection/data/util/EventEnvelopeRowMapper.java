package com.soul.fin.accounting.projection.data.util;


import com.soul.fin.common.projection.model.EventEnvelope;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;

public final class EventEnvelopeRowMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private EventEnvelopeRowMapper() {
        // utility class
    }

    public static EventEnvelope map(Row row, RowMetadata metadata) {
        return new EventEnvelope(
                row.get("event_id", UUID.class),
                row.get("global_position", Long.class),
                row.get("event_type", String.class),
                row.get("event_schema_version", Long.class),
                row.get("aggregate_id", UUID.class),
                row.get("aggregate_type", String.class),
                row.get("aggregate_version", Long.class),
                row.get("payload", String.class),
                row.get("metadata", String.class),
                row.get("occurred_at", Instant.class)
        );
    }

    private static JsonNode toJson(String json) {
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to parse JSON from event store",
                    e
            );
        }
    }
}
