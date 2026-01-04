package com.soul.fin.common.projection.engine;

import com.soul.fin.common.projection.api.Projection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class ProjectionEngine {

    private final List<Projection> projections;
    private final ProjectionRunner runner;

    public ProjectionEngine(
            List<Projection> projections,
            ProjectionRunner runner
    ) {
        this.projections = projections;
        this.runner = runner;
    }

    public Mono<Void> start() {
        return Flux.fromIterable(projections)
                .flatMap(runner::run)
                .then();
    }
}


