package com.soul.fin.common.core.event;

import com.soul.fin.common.core.entity.Metadata;

import java.time.Instant;
import java.util.UUID;

public class EventMetadata implements Metadata {
    private UUID eventId;
    private UUID correlationId;
    private UUID causationId;
    private UUID commandId;
    private String actor;
    private String tenantId;
    private Instant occurredAt;

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public UUID getCausationId() {
        return causationId;
    }

    public void setCausationId(UUID causationId) {
        this.causationId = causationId;
    }

    public UUID getCommandId() {
        return commandId;
    }

    public void setCommandId(UUID commandId) {
        this.commandId = commandId;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

//    public long getSchemaVersion() {
//        return schemaVersion;
//    }
//
//    public void setSchemaVersion(long schemaVersion) {
//        this.schemaVersion = schemaVersion;
//    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }
}
