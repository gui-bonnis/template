package com.soul.fin.common.projection;

import com.soul.fin.common.projection.api.Projection;
import reactor.core.publisher.Mono;

public interface ProjectionOrchestrator {

    Mono<Void> runProjection(Projection projection);

    Mono<Void> runAll();
}


