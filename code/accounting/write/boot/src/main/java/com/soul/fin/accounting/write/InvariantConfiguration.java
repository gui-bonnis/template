package com.soul.fin.accounting.write;

import com.soul.fin.accounting.write.customer.entity.Customer;
import com.soul.fin.accounting.write.customer.invariants.BlackListNameInvariant;
import com.soul.fin.common.command.application.invariants.DefaultInvariantRegistry;
import com.soul.fin.common.command.application.invariants.InvariantRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InvariantConfiguration {

    @Bean
    public InvariantRegistry invariantRegistry(
            BlackListNameInvariant blackListNameInvariant
            //, LedgerBalancedInvariant ledgerBalancedInvariant
    ) {
        DefaultInvariantRegistry registry =
                new DefaultInvariantRegistry();

        registry.register(
                Customer.class,
                blackListNameInvariant
        );
//
//        registry.register(
//                Ledger.class,
//                ledgerBalancedInvariant
//        );

        return registry;
    }
}

