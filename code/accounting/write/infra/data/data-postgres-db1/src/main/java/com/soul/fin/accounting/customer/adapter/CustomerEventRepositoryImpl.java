package com.soul.fin.accounting.customer.adapter;

import com.soul.fin.accounting.customer.entity.Customer;
import com.soul.fin.accounting.customer.entity.CustomerEventEntity;
import com.soul.fin.accounting.customer.ports.output.repository.CustomerEventRepository;
import com.soul.fin.accounting.customer.repository.CustomerJPAEventStoreRepository;
import com.soul.fin.accounting.customer.serializer.CustomerEventSerializer;
import com.soul.fin.accounting.customer.upcaster.CustomerEventUpcaster;
import com.soul.fin.accounting.customer.vo.CustomerId;
import com.soul.fin.common.core.event.DomainEvent;
import com.soul.fin.common.core.event.Metadata;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
public class CustomerEventRepositoryImpl implements CustomerEventRepository {

    private final CustomerJPAEventStoreRepository repository;
    private final CustomerEventSerializer serializer;
    private final CustomerEventUpcaster upcaster;

    public CustomerEventRepositoryImpl(CustomerJPAEventStoreRepository repository, CustomerEventSerializer serializer, CustomerEventUpcaster upcaster) {
        this.repository = repository;
        this.serializer = serializer;
        this.upcaster = upcaster;
    }

    @Override
    public Flux<DomainEvent> load(CustomerId customerId) {
        return repository
                .findByCustomerIdOrderByAggregateVersion(customerId.getValue())
                .map(upcaster::upcast);
    }

    @Override
    public Mono<Void> append(
            Customer customer,
            Metadata metadata
    ) {
        List<CustomerEventEntity> entities =
                customer.pullEvents().stream()
                        .map(domainEvent -> serializer.serialize(
                                        customer.getId().getValue(),
                                        customer.getClass().getSimpleName(),
                                        customer.getAggregateVersion(),
                                        UUID.randomUUID(),
                                        metadata,
                                        domainEvent
                                )
                        ).toList();

        return repository.saveAll(entities).then();
    }
}