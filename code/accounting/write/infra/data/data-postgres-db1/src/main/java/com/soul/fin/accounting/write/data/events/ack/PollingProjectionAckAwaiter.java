package com.soul.fin.accounting.write.data.events.ack;

import com.soul.fin.common.projection.api.ProjectionAckAwaiter;
import com.soul.fin.common.projection.api.ReadProjectionAckStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Component
public class PollingProjectionAckAwaiter implements ProjectionAckAwaiter {

    private final ReadProjectionAckStore store;

    public PollingProjectionAckAwaiter(ReadProjectionAckStore store) {
        this.store = store;
    }

    @Override
    public Mono<Void> await(UUID eventId,
                            String projectionName,
                            Duration timeout) {

        return Mono.defer(() ->
                        store.exists(eventId, projectionName)
                                .filter(Boolean::booleanValue)
                )
                .repeatWhenEmpty(repeat ->
                        repeat.delayElements(Duration.ofMillis(50))
                )
                .timeout(timeout)
                .then();
    }
}

