package com.soul.fin.accounting.projection;

import com.soul.fin.common.projection.api.EventStream;
import com.soul.fin.common.projection.api.Projection;
import com.soul.fin.common.projection.api.ProjectionOffsetStore;
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
            TransactionManager txManager,
            ProjectionErrorHandler errorHandler
    ) {
        return new ProjectionRunner(
                eventStream,
                offsetStore,
                txManager,
                errorHandler
        );
    }
}

