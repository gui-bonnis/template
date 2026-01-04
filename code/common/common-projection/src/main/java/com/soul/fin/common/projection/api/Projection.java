package com.soul.fin.common.projection.api;


import com.soul.fin.common.projection.model.EventEnvelope;
import com.soul.fin.common.projection.model.ProjectionBatchConfig;
import com.soul.fin.common.projection.retry.RetryPolicy;
import reactor.core.publisher.Mono;

public interface Projection {

    String name();

    boolean supports(EventEnvelope event);

    Mono<Void> project(EventEnvelope event);

    default ProjectionBatchConfig batchConfig() {
        return ProjectionBatchConfig.defaultConfig();
    }

    default RetryPolicy retryPolicy() {
        return RetryPolicy.defaultPolicy();
    }
}

