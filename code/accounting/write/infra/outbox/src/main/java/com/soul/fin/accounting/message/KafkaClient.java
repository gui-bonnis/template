package com.soul.fin.accounting.message;

public interface KafkaClient {
    void send(String topic, String payload, String metadata);
}
