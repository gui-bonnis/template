package com.soul.fin.accounting.customer.policy;

import com.soul.fin.accounting.customer.event.CustomerRegisteredEvent;
import com.soul.fin.common.application.policy.AsyncPolicy;
import com.soul.fin.common.application.policy.PolicyContext;
import com.soul.fin.common.application.policy.Severity;
import com.soul.fin.common.application.policy.effects.PolicyAlert;
import com.soul.fin.common.application.policy.risk.RiskScore;

public final class AmlMonitoringPolicy
        implements AsyncPolicy<CustomerRegisteredEvent> {

    @Override
    public void onEvent(
            CustomerRegisteredEvent event,
            PolicyContext<CustomerRegisteredEvent> ctx
    ) {
        RiskScore score =
                ctx.services().riskService().evaluate(event);

        if (score.isHigh()) {
            ctx.effects().alert(
                    new PolicyAlert(
                            "AML_HIGH_RISK",
                            "High AML risk on account " + event.aggregateId(),
                            Severity.HARD_BLOCK,
                            ctx.now()
                    )
            );
        }
    }
}
