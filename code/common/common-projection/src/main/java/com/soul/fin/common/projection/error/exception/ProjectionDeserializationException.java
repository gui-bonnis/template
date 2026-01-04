package com.soul.fin.common.projection.error.exception;

import com.soul.fin.common.projection.model.EventEnvelope;

public class ProjectionDeserializationException extends RuntimeException {

    private final String projectionName;
    private final String eventType;
    private final long schemaVersion;
    private final long eventPosition;

    public ProjectionDeserializationException(
            String projectionName,
            EventEnvelope event,
            Throwable cause
    ) {
        super(
                "Failed to deserialize event payload for projection '%s' " +
                        "[eventType=%s, schemaVersion=%d, position=%d]"
                                .formatted(
                                        projectionName,
                                        event.eventType(),
                                        event.eventSchemaVersion(),
                                        event.eventPosition()
                                ),
                cause
        );

        this.projectionName = projectionName;
        this.eventType = event.eventType();
        this.schemaVersion = event.eventSchemaVersion();
        this.eventPosition = event.eventPosition();
    }

    public String projectionName() {
        return projectionName;
    }

    public String eventType() {
        return eventType;
    }

    public long schemaVersion() {
        return schemaVersion;
    }

    public long eventPosition() {
        return eventPosition;
    }
}

