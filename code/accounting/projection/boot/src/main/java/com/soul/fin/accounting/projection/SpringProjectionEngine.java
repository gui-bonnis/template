package com.soul.fin.accounting.projection;

import com.soul.fin.common.projection.engine.ProjectionEngine;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class SpringProjectionEngine {

    private final ProjectionEngine engine;

    SpringProjectionEngine(ProjectionEngine engine) {
        this.engine = engine;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        engine.start();
    }
}

