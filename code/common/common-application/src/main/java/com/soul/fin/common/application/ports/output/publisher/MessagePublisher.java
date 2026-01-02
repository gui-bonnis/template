package com.soul.fin.common.application.ports.output.publisher;

import com.soul.fin.common.application.dto.EventEnvelope;
import reactor.core.publisher.Flux;

import java.util.List;

public interface MessagePublisher {
    Flux<EventEnvelope> publish(List<EventEnvelope> events);
}
