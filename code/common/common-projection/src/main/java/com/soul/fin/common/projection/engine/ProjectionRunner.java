package com.soul.fin.common.projection.engine;

import com.soul.fin.common.projection.api.EventStream;
import com.soul.fin.common.projection.api.Projection;
import com.soul.fin.common.projection.api.ProjectionOffsetStore;
import com.soul.fin.common.projection.error.ProjectionErrorHandler;
import com.soul.fin.common.projection.model.EventEnvelope;
import com.soul.fin.common.projection.model.ProjectionBatchConfig;
import com.soul.fin.common.projection.retry.RetryPolicy;
import org.springframework.dao.TransientDataAccessException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public final class ProjectionRunner {

    private final EventStream eventStream;
    private final ProjectionOffsetStore offsetStore;
    private final TransactionManager txManager;
    private final ProjectionErrorHandler errorHandler;

    public ProjectionRunner(
            EventStream eventStream,
            ProjectionOffsetStore offsetStore,
            TransactionManager txManager, ProjectionErrorHandler errorHandler
    ) {
        this.eventStream = eventStream;
        this.offsetStore = offsetStore;
        this.txManager = txManager;
        this.errorHandler = errorHandler;
    }

    Mono<Void> run(Projection projection) {

        ProjectionBatchConfig cfg = projection.batchConfig();

        return offsetStore.loadOffset(projection.name())
                .defaultIfEmpty(0L)
                .flatMapMany(offset ->
                        eventStream.streamFrom(offset)
                                .filter(projection::supports)
                                .bufferTimeout(cfg.maxEvents(), cfg.maxWait())
                                .concatMap(batch ->
                                        processBatch(projection, batch)
                                )
                )
                .then();
    }

    private Retry retrySpec(RetryPolicy policy) {
        return Retry
                .backoff(policy.maxAttempts(), policy.initialBackoff())
                .maxBackoff(policy.maxBackoff())
                .jitter(policy.jitter())
                .filter(this::isRetryable)
                .onRetryExhaustedThrow((spec, signal) -> signal.failure());
    }

    private boolean isRetryable(Throwable error) {
        return error instanceof TransientDataAccessException
                || error instanceof TimeoutException
                || error instanceof IOException;
    }


    private Mono<Void> processBatch(
            Projection projection,
            List<EventEnvelope> batch
    ) {
        if (batch.isEmpty()) return Mono.empty();

        long lastPosition = batch.getLast().eventPosition();

        return txManager.inTransaction(
                Flux.fromIterable(batch)
                        .concatMap(event ->
                                projection.project(event)
                                        .retryWhen(retrySpec(projection.retryPolicy()))
                                        .onErrorResume(error ->
                                                errorHandler.quarantine(
                                                        projection,
                                                        event,
                                                        error
                                                )
                                        )
                        )
                        .then(offsetStore.saveOffset(
                                projection.name(),
                                lastPosition
                        ))
        );
    }

}

