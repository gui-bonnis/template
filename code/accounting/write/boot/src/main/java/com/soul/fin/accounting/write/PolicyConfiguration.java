package com.soul.fin.accounting.write;

import com.soul.fin.accounting.write.customer.dto.command.RegisterCustomerCommand;
import com.soul.fin.accounting.write.customer.event.CustomerRegisteredEvent;
import com.soul.fin.accounting.write.customer.policy.AmlMonitoringPolicy;
import com.soul.fin.accounting.write.customer.policy.CustomerNameAlreadyRegistered;
import com.soul.fin.common.command.application.policy.registry.DefaultPolicyRegistry;
import com.soul.fin.common.command.application.policy.registry.PolicyRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PolicyConfiguration {

    @Bean
    public PolicyRegistry policyRegistry(
//            WithdrawalReadModel withdrawalReadModel,
//            CustomerReadModel customerReadModel
    ) {
        DefaultPolicyRegistry registry = new DefaultPolicyRegistry();

        registry.register(
                RegisterCustomerCommand.class,
                //new CustomerNameAlreadyRegistered(customerReadModel)
                new CustomerNameAlreadyRegistered()
        );

//        registry.register(
//                WithdrawMoney.class,
//                new DailyWithdrawalLimitPolicy(withdrawalReadModel)
//        );

        registry.registerEventPolicy(
                CustomerRegisteredEvent.class,
                new AmlMonitoringPolicy()
        );

        return registry;
    }
}

