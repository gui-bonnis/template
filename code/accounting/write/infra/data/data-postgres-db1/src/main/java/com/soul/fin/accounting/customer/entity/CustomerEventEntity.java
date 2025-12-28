package com.soul.fin.accounting.customer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
@Table(name = "customer", schema = "customer_event_store")
public class CustomerEventEntity {

    @Id
    @Column("GLOBAL_POSITION")
    private Long id;

    @Column("AGGREGATE_ID")
    private UUID aggregateId;
    @Column("AGGREGATE_TYPE")
    private String aggregateType;
    @Column("AGGREGATE_VERSION")
    private Long aggregateVersion;
    @Column("EVENT_ID")
    private UUID eventId;
    @Column("EVENT_TYPE")
    private String eventType;
    @Column("PAYLOAD")
    private String payload;
    @Column("METADATA")
    private String metadata;
    @Column("OCCURRED_AT")
    private Instant occurredAt;

    public CustomerEventEntity() {
    }

    public CustomerEventEntity(Long id,
                               UUID aggregateId,
                               String aggregateType,
                               Long aggregateVersion,
                               UUID eventId,
                               String eventType,
                               String payload,
                               String metadata,
                               Instant occurredAt) {
        this.id = id;
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.aggregateVersion = aggregateVersion;
        this.eventId = eventId;
        this.eventType = eventType;
        this.payload = payload;
        this.metadata = metadata;
        this.occurredAt = occurredAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

