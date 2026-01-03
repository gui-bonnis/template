package com.soul.fin.accounting.write.data.outbox.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
@Table(name = "customer", schema = "outbox")
public class OutboxEntity {

    @Id
    private UUID id;
    private UUID aggregateId;
    private UUID eventId;
    private String type;
    private String payload;
    private String metadata;
    private Instant createdAt;
    private Instant processedAt;
    private String status;

    public OutboxEntity() {
    }

    public OutboxEntity(UUID aggregateId, UUID eventId, String type, String payload, String metadata, Instant createdAt, Instant processedAt, String status) {
        this.aggregateId = aggregateId;
        this.eventId = eventId;
        this.type = type;
        this.payload = payload;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

