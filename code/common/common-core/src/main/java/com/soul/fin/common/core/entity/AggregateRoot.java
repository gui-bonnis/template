package com.soul.fin.common.core.entity;

import com.soul.fin.common.core.vo.BaseId;

public abstract class AggregateRoot<ID extends BaseId<?>> extends BaseEntity<ID> {

    private long aggregateVersion;

    public long getAggregateVersion() {
        return aggregateVersion;
    }

    protected void setAggregateVersion(long aggregateVersion) {
        this.aggregateVersion = aggregateVersion;
    }

    public long increaseSchemaVersion() {
        this.aggregateVersion = this.aggregateVersion + 1L;
        return this.aggregateVersion;
    }
}
