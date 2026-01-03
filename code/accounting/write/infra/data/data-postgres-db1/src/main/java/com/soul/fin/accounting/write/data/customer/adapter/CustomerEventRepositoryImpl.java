package com.soul.fin.accounting.write.data.customer.adapter;


import com.soul.fin.accounting.write.customer.ports.output.repository.CustomerEventRepository;
import com.soul.fin.accounting.write.customer.vo.CustomerId;
import com.soul.fin.accounting.write.data.customer.repository.CustomerEventReactiveRepository;
import com.soul.fin.accounting.write.data.customer.serializer.CustomerEventSerializer;
import com.soul.fin.accounting.write.data.customer.upcaster.CustomerEventUpCaster;
import com.soul.fin.accounting.write.data.customer.writer.PostgresEventStoreWriter;
import com.soul.fin.common.application.dto.EventEnvelope;
import com.soul.fin.common.core.event.DomainEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CustomerEventRepositoryImpl implements CustomerEventRepository {

    private final CustomerEventReactiveRepository repository;
    private final PostgresEventStoreWriter writer;
    private final CustomerEventSerializer serializer;
    private final CustomerEventUpCaster upCaster;

    public CustomerEventRepositoryImpl(CustomerEventReactiveRepository repository, PostgresEventStoreWriter writer, CustomerEventSerializer serializer, CustomerEventUpCaster upCaster) {
        this.repository = repository;
        this.writer = writer;
        this.serializer = serializer;
        this.upCaster = upCaster;
    }

    @Override
    public Flux<DomainEvent> load(CustomerId customerId) {
        return repository
                .findByAggregateIdOrderByAggregateVersion(customerId.getValue())
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
                                eventEnvelope.payload()
                        )
                )
                .thenReturn(eventEnvelope);
    }

}