package com.soul.fin.accounting.message;

import com.soul.fin.accounting.common.MessagePublisher;
import com.soul.fin.accounting.common.OutboxMessage;

public class KafkaMessagePublisher implements MessagePublisher {

    private final KafkaClient client;

    public KafkaMessagePublisher(KafkaClient client) {
        this.client = client;
    }

    @Override
    public void publish(OutboxMessage message) {
        client.send(
                message.type(),
                message.payload(),
                message.metadata()
        );
    }
}
