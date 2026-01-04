package com.soul.fin.accounting.read;

import com.soul.fin.common.projection.api.Projection;
import com.soul.fin.common.projection.engine.ProjectionEngine;
import com.soul.fin.common.projection.engine.ProjectionRunner;
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
}

