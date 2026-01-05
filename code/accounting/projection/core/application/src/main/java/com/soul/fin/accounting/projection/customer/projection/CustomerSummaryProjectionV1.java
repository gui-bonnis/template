package com.soul.fin.accounting.projection.customer.projection;

import com.soul.fin.accounting.projection.customer.payload.CustomerRegisteredPayloadV1;
import com.soul.fin.accounting.projection.customer.payload.CustomerRegisteredPayloadV2;
import com.soul.fin.accounting.projection.customer.payload.CustomerValidatedPayloadV1;
import com.soul.fin.accounting.projection.customer.repository.CustomerSummaryRepositoryV1;
import com.soul.fin.accounting.read.customer.entity.CustomerSummaryV1;
import com.soul.fin.common.projection.dsl.ProjectionDsl;
import org.springframework.stereotype.Component;

@Component
public class CustomerSummaryProjectionV1 extends ProjectionDsl {

    private final CustomerSummaryRepositoryV1 repository;

    public CustomerSummaryProjectionV1(CustomerSummaryRepositoryV1 repository) {
        this.repository = repository;
    }

    @Override
    public String name() {
        return "customer_summary_v1";
    }

    @Override
    protected void registerHandlers() {

        on(
                "CustomerRegisteredEvent",
                1,
                CustomerRegisteredPayloadV1.class,
                (payload, env) ->
                        repository.insert(
                                new CustomerSummaryV1(
                                        payload.aggregateId(),
                                        payload.eventId(),
                                        payload.aggregateVersion(),
                                        payload.name(),
                                        payload.version(),
                                        env.occurredAt()
                                )
                        )
        );

        on(
                "CustomerRegisteredEvent",
                2,
                CustomerRegisteredPayloadV2.class,
                (payload, env) ->
                        repository.insert(
                                new CustomerSummaryV1(
                                        payload.aggregateId(),
                                        payload.eventId(),
                                        payload.aggregateVersion(),
                                        payload.name(),
                                        payload.version(),
                                        env.occurredAt()
                                )
                        )
        );

        on(
                "CustomerValidatedEvent",
                1,
                CustomerValidatedPayloadV1.class,
                (payload, env) ->
                        repository.increaseAvailable(
                                payload.customerId(),
                                payload.name(),
                                payload.version(),
                                env.occurredAt()
                        )
        );
    }
}