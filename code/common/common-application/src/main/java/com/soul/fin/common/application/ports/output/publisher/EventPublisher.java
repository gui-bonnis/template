package com.soul.fin.common.application.ports.output.publisher;

import com.soul.fin.common.application.dto.EventEnvelope;
import reactor.core.publisher.Mono;

import java.util.List;

public interface EventPublisher {

    //Mono<Void> publish(Flux<EventEnvelope> events);
    Mono<Void> publish(List<EventEnvelope> events);
}
