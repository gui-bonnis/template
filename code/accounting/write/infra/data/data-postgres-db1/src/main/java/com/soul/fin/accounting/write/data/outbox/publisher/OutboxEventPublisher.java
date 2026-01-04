package com.soul.fin.accounting.write.data.outbox.publisher;

import com.soul.fin.accounting.write.data.outbox.entity.OutboxEntity;
import com.soul.fin.accounting.write.data.outbox.writer.PostgresOutboxWriter;
import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.command.application.ports.output.publisher.MessagePublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

@Component
public class OutboxEventPublisher implements MessagePublisher {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final PostgresOutboxWriter writer;

    public OutboxEventPublisher(PostgresOutboxWriter writer) {
        this.writer = writer;
    }

    @Override
    public Flux<EventEnvelope> publish(List<EventEnvelope> events) {
        return Flux.fromIterable(events)
                .flatMap(eventEnvelope -> {
                    var outboxEntity = new OutboxEntity(
                            eventEnvelope.aggregateId(),
                            eventEnvelope.eventId(),
                            eventEnvelope.eventPosition(),
                            eventEnvelope.eventType(),
                            OBJECT_MAPPER.writeValueAsString(eventEnvelope.payload()),
                            OBJECT_MAPPER.writeValueAsString(eventEnvelope.metadata()),
                            Instant.now(),
                            null,
                            "PENDENT"
                    );
                    return this.writer.append(outboxEntity)
                            .thenReturn(eventEnvelope);
                });

    }

}
