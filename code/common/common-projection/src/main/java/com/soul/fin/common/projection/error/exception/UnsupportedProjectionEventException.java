package com.soul.fin.common.projection.error.exception;


import com.soul.fin.common.projection.model.EventEnvelope;

public class UnsupportedProjectionEventException extends RuntimeException {

    private final String projectionName;
    private final String eventType;
    private final long schemaVersion;
    private final long eventPosition;

    public UnsupportedProjectionEventException(
            String projectionName,
            EventEnvelope event
    ) {
        super(
                "Projection '" + projectionName + "' does not support event " +
                        "[eventType=" + event.eventType() +
                        ", schemaVersion=" + event.eventSchemaVersion() +
                        ", position=" + event.eventPosition() + "]"
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
