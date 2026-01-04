package com.soul.fin.accounting.write.data.events.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
// check postgres connection to see if is direct to a database, if so, we can change schema and table naming
@Table(name = "events", schema = "accounting_events_store")
public class EventsEntity {

    @Id
    private Long globalPosition;
    private UUID aggregateId;
    private String aggregateType;
    private Long aggregateVersion;
    private UUID eventId;
    private String eventType;
    private Long eventSchemaVersion;
    private String payload;
    private String metadata;
    private Instant occurredAt;

    public EventsEntity() {
    }

    public EventsEntity(Long globalPosition,
                        UUID aggregateId,
                        String aggregateType,
                        Long aggregateVersion,
                        UUID eventId,
                        String eventType,
                        Long eventSchemaVersion,
                        String payload,
                        String metadata,
                        Instant occurredAt) {
        this.globalPosition = globalPosition;
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.aggregateVersion = aggregateVersion;
        this.eventId = eventId;
        this.eventType = eventType;
        this.eventSchemaVersion = eventSchemaVersion;
        this.payload = payload;
        this.metadata = metadata;
        this.occurredAt = occurredAt;
    }

    public Long getGlobalPosition() {
        return globalPosition;
    }

    public void setGlobalPosition(Long globalPosition) {
        this.globalPosition = globalPosition;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public Long getAggregateVersion() {
        return aggregateVersion;
    }

    public void setAggregateVersion(Long aggregateVersion) {
        this.aggregateVersion = aggregateVersion;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getEventSchemaVersion() {
        return eventSchemaVersion;
    }

    public void setEventSchemaVersion(Long eventSchemaVersion) {
        this.eventSchemaVersion = eventSchemaVersion;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }
}

