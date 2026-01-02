package com.soul.fin.common.application.event.middleware;

import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.application.event.EventMiddleware;
import com.soul.fin.common.application.event.EventMiddlewareChain;
import com.soul.fin.common.application.ports.output.publisher.MessagePublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public final class MessageDispatchMiddleware implements EventMiddleware {

    private final MessagePublisher publisher;

    public MessageDispatchMiddleware(MessagePublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public Mono<Void> handle(
            List<EventEnvelope> events,
            EventMiddlewareChain next
    ) {
        return publisher.publish(events)
                .then(next.proceed(events));
    }
}

