package com.soul.fin.common.projection;

import com.soul.fin.common.projection.api.EventStream;
import com.soul.fin.common.projection.api.Projection;
import com.soul.fin.common.projection.api.ProjectionOffsetStore;
import com.soul.fin.common.projection.api.WriteProjectionAckStore;
import com.soul.fin.common.projection.engine.TransactionManager;
import com.soul.fin.common.projection.model.ProjectionAck;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;


public final class DefaultProjectionOrchestrator
        implements ProjectionOrchestrator {

    private final EventStream eventStream;
    private final ProjectionOffsetStore offsetStore;
    private final WriteProjectionAckStore ackStore;
    private final TransactionManager tx;
    private final List<Projection> projections;

    public DefaultProjectionOrchestrator(
            EventStream eventStream,
            ProjectionOffsetStore offsetStore, WriteProjectionAckStore ackStore,
            TransactionManager tx,
            List<Projection> projections
    ) {
        this.eventStream = eventStream;
        this.offsetStore = offsetStore;
        this.ackStore = ackStore;
        this.tx = tx;
        this.projections = projections;
    }

    @Override
    public Mono<Void> runProjection(Projection projection) {

        return tx.inTransaction(
                offsetStore
                        .loadOffset(projection.name())
                        .defaultIfEmpty(0L)
                        .flatMapMany(
                                lastOffset ->
                                        eventStream
                                                .streamFrom(lastOffset)
                                                .filter(projection::supports)
                                                .take(projection.batchConfig().maxEvents())
                                                .concatMap(event ->
                                                        projection.project(event)
                                                                .then(
                                                                        offsetStore.saveOffset(
                                                                                projection.name(),
                                                                                event.eventPosition()
                                                                        )
                                                                )
                                                                .then(
                                                                        ackStore.record(
                                                                                new ProjectionAck(
                                                                                        event.eventId(),
                                                                                        projection.name(),
                                                                                        Instant.now()
                                                                                )
                                                                        )
                                                                )
                                                )

                        )
                        .then()
        );

    }

    @Override
    public Mono<Void> runAll() {
        return Flux.fromIterable(projections)
                .concatMap(this::runProjection)
                .then();
    }
}



