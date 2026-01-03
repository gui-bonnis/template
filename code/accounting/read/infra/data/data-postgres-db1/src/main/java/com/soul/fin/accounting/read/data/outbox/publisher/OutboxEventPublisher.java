package com.soul.fin.accounting.read.data.outbox.publisher;

import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.application.ports.output.publisher.MessagePublisher;
import reactor.core.publisher.Flux;

import java.util.List;

public class OutboxEventPublisher implements MessagePublisher {
    
    @Override
    public Flux<EventEnvelope> publish(List<EventEnvelope> events) {
        return null;

    }

}
