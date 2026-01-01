package com.soul.fin.accounting.common;

public interface OutboxWriter {
    void save(OutboxMessage message);
}
