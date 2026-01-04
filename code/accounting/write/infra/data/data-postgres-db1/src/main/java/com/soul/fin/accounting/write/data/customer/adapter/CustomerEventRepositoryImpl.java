package com.soul.fin.accounting.write.data.customer.adapter;


import com.soul.fin.accounting.write.customer.ports.output.repository.CustomerEventRepository;
import com.soul.fin.accounting.write.customer.vo.CustomerId;
import com.soul.fin.accounting.write.data.customer.upcaster.CustomerEventUpCaster;
import com.soul.fin.accounting.write.data.events.repository.EventsReactiveRepository;
import com.soul.fin.accounting.write.data.events.serializer.EventsSerializer;
import com.soul.fin.accounting.write.data.events.writer.PostgresEventStoreWriter;
import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.core.event.DomainEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CustomerEventRepositoryImpl implements CustomerEventRepository {

    private final EventsReactiveRepository repository;
    private final PostgresEventStoreWriter writer;
    private final EventsSerializer serializer;
    private final CustomerEventUpCaster upCaster;

    public CustomerEventRepositoryImpl(EventsReactiveRepository repository,
                                       PostgresEventStoreWriter writer,
                                       EventsSerializer serializer,
                                       CustomerEventUpCaster upCaster) {
        this.repository = repository;
        this.writer = writer;
        this.serializer = serializer;
        this.upCaster = upCaster;
    }

    @Override
    public Flux<DomainEvent> load(CustomerId aggregateId) {
        return repository
                .findByAggregateIdOrderByAggregateVersion(aggregateId.getValue())
                .map(upCaster::upcast);
    }

    @Override
    public Mono<EventEnvelope> append(EventEnvelope eventEnvelope) {
        return writer.append(
                        serializer.serialize(
                                eventEnvelope.aggregateId(),
                                eventEnvelope.aggregateType(),
                                eventEnvelope.aggregateVersion(),
                                eventEnvelope.eventId(),
                                eventEnvelope.metadata(),
                                eventEnvelope.payload(),
                                eventEnvelope.occurredAt()
                        )
                )
                .map(global_position ->
                        new EventEnvelope(
                                eventEnvelope.eventId(),
                                global_position,
                                eventEnvelope.eventType(),
                                eventEnvelope.eventSchemaVersion(),
                                eventEnvelope.aggregateId(),
                                eventEnvelope.aggregateType(),
                                eventEnvelope.aggregateVersion(),
                                eventEnvelope.payload(),
                                eventEnvelope.metadata(),
                                eventEnvelope.occurredAt()
                        ));
    }

}