package com.soul.fin.common.application.event;

import com.soul.fin.common.application.dto.EventEnvelope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public final class EventPipeline {

    private final List<EventMiddleware> middlewares;

    public EventPipeline(List<EventMiddleware> middlewares) {
        this.middlewares = middlewares;
    }

    public Mono<Void> execute(List<EventEnvelope> events) {
        return new DefaultChain(middlewares, 0).proceed(events);
    }

    private static final class DefaultChain
            implements EventMiddlewareChain {

        private final List<EventMiddleware> middlewares;
        private final int index;

        DefaultChain(List<EventMiddleware> middlewares, int index) {
            this.middlewares = middlewares;
            this.index = index;
        }

        @Override
        public Mono<Void> proceed(List<EventEnvelope> events) {
            if (index >= middlewares.size()) {
                return Mono.empty();
            }

            EventMiddleware current = middlewares.get(index);
            return current.handle(
                    events,
                    new DefaultChain(middlewares, index + 1)
            );
        }
    }
}

