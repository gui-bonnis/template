package com.soul.fin.accounting.projection;

import com.soul.fin.common.projection.DefaultProjectionOrchestrator;
import com.soul.fin.common.projection.ProjectionOrchestrator;
import com.soul.fin.common.projection.api.EventStream;
import com.soul.fin.common.projection.api.Projection;
import com.soul.fin.common.projection.api.ProjectionOffsetStore;
import com.soul.fin.common.projection.api.WriteProjectionAckStore;
import com.soul.fin.common.projection.engine.ProjectionEngine;
import com.soul.fin.common.projection.engine.ProjectionRunner;
import com.soul.fin.common.projection.engine.TransactionManager;
import com.soul.fin.common.projection.error.ProjectionErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProjectionConfiguration {

    @Bean
    ProjectionEngine projectionEngine(
            List<Projection> projections,
            ProjectionRunner runner
    ) {
        return new ProjectionEngine(projections, runner);
    }

    @Bean
    ProjectionRunner projectionRunner(
            EventStream eventStream,
            ProjectionOffsetStore offsetStore,
            WriteProjectionAckStore ackStore,
            TransactionManager txManager,
            ProjectionErrorHandler errorHandler
    ) {
        return new ProjectionRunner(
                eventStream,
                offsetStore,
                ackStore,
                txManager,
                errorHandler
        );
    }

    @Bean
    ProjectionOrchestrator projectionOrchestrator(
            EventStream eventStream,
            ProjectionOffsetStore offsetStore,
            WriteProjectionAckStore ackStore,
            TransactionManager txManager,
            List<Projection> projections
    ) {
        return new DefaultProjectionOrchestrator(
                eventStream,
                offsetStore,
                ackStore,
                txManager,
                projections
        );
    }

}

