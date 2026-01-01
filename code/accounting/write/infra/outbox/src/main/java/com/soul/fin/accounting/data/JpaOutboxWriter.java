package com.soul.fin.accounting.data;

import com.soul.fin.accounting.common.OutboxMessage;
import com.soul.fin.accounting.common.OutboxWriter;

public class JpaOutboxWriter implements OutboxWriter {
    private final JpaOutboxRepository repository;

    public JpaOutboxWriter(JpaOutboxRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(OutboxMessage message) {
        repository.save(JpaOutboxEntity.from(message));
    }
}
