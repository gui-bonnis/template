package com.soul.fin.accounting.projection;

import com.soul.fin.common.projection.error.ProjectionErrorHandler;
import com.soul.fin.common.projection.ports.PoisonEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ProjectionInfraConfiguration {

    @Bean
    ProjectionErrorHandler projectionErrorHandler(
            PoisonEventRepository repo
    ) {
        return new ProjectionErrorHandler(repo);
    }
}

