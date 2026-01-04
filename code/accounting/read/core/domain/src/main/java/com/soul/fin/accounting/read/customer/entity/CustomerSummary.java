package com.soul.fin.accounting.read.customer.entity;

import java.util.UUID;

public class CustomerSummary {
    private UUID id;
    private String name;
    private long version;

    public CustomerSummary(UUID id, String name, long version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
