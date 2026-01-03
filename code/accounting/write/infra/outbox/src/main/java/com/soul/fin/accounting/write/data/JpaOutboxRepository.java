package com.soul.fin.accounting.data;


import com.soul.fin.accounting.common.OutboxStatus;

import java.util.List;
import java.util.UUID;

public interface JpaOutboxRepository {

    void save(JpaOutboxEntity entity);

    List<JpaOutboxEntity> findPending(int limit);

    void updateStatus(UUID id, OutboxStatus status);
}
