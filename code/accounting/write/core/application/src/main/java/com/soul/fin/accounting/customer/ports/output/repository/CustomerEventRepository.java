package com.soul.fin.accounting.customer.ports.output.repository;

import com.soul.fin.accounting.customer.entity.Customer;
import com.soul.fin.accounting.customer.vo.CustomerId;
import com.soul.fin.common.core.event.DomainEvent;
import com.soul.fin.common.core.event.Metadata;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public interface CustomerEventRepository {

    Flux<DomainEvent> load(CustomerId customerId);

    Mono<Void> append(
            Customer customer,
            Metadata metadata
    );

}
