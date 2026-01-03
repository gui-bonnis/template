package com.soul.fin.accounting.read;


import com.soul.fin.accounting.read.customer.mapper.CustomerAggregateFactory;
import com.soul.fin.accounting.read.data.customer.adapter.CustomerEventRepositoryImpl;
import com.soul.fin.accounting.read.data.outbox.publisher.OutboxEventPublisher;
import com.soul.fin.common.application.invariants.DefaultInvariantRegistry;
import com.soul.fin.common.application.invariants.InvariantRegistry;
import com.soul.fin.common.application.mapper.AggregateFactory;
import com.soul.fin.common.application.policy.registry.DefaultPolicyRegistry;
import com.soul.fin.common.application.policy.registry.PolicyRegistry;
import com.soul.fin.common.application.policy.service.DefaultPolicyServices;
import com.soul.fin.common.application.policy.service.PolicyServices;
import com.soul.fin.common.application.ports.output.publisher.MessagePublisher;
import com.soul.fin.common.application.ports.output.repository.EventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {


    @Bean
    public PolicyRegistry policyRegistry() {
        return new DefaultPolicyRegistry();
    }

    @Bean
    public PolicyServices policyServices() {
        return new DefaultPolicyServices(null, null, null, null);
    }

    @Bean
    public MessagePublisher messagePublisher() {
        return new OutboxEventPublisher();
    }

    @Bean
    public InvariantRegistry invariantRegistry() {
        return new DefaultInvariantRegistry();
    }

    @Bean
    public EventRepository<?> eventRepository() {
        return new CustomerEventRepositoryImpl();
    }

    @Bean
    public AggregateFactory<?> aggregateFactory() {
        return new CustomerAggregateFactory();
    }
}
