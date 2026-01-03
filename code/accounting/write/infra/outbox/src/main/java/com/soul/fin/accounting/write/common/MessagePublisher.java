package com.soul.fin.accounting.common;

public interface MessagePublisher {
    void publish(OutboxMessage message);
}
