package com.soul.fin.accounting;

import com.soul.fin.common.application.policy.feature.FeatureFlags;
import com.soul.fin.common.application.policy.risk.RiskService;
import com.soul.fin.common.application.policy.service.DefaultPolicyServices;
import com.soul.fin.common.application.policy.service.PolicyServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class PolicyServicesConfiguration {

    @Bean
    public PolicyServices policyServices(
            FeatureFlags featureFlags,
            RiskService riskService
    ) {
        return new DefaultPolicyServices(
                featureFlags,
                riskService,
                Clock.systemUTC()
        );
    }
}

