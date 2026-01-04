package com.soul.fin.accounting.read.data.customer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "customer_summary_current", schema = "accounting_projections")
public class CustomerSummaryEntity {

    @Id
    private UUID id;
    private long version;
    private String name;


    public CustomerSummaryEntity() {
    }

    public CustomerSummaryEntity(UUID id, long version, String name) {
        this.id = id;
        this.version = version;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

