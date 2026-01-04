package com.soul.fin.common.projection.error;

import com.soul.fin.common.projection.api.Projection;
import com.soul.fin.common.projection.model.EventEnvelope;
import com.soul.fin.common.projection.ports.PoisonEventRepository;
import reactor.core.publisher.Mono;


public class ProjectionErrorHandler {

    private final PoisonEventRepository poisonRepo;

    public ProjectionErrorHandler(PoisonEventRepository poisonRepo) {
        this.poisonRepo = poisonRepo;
    }

    public Mono<Void> quarantine(
            Projection projection,
            EventEnvelope event,
            Throwable error
    ) {
        return poisonRepo.save(
                projection.name(),
                event,
                error
        );
    }
}

