package com.soul.fin.accounting.projection;

import com.soul.fin.common.projection.ProjectionOrchestrator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public final class ProjectionScheduler {

    private final ProjectionOrchestrator orchestrator;

    public ProjectionScheduler(ProjectionOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @Scheduled(fixedDelayString = "${projection.tick-ms:500}")
    public void tick() {
        orchestrator.runAll().subscribe();
    }
}
