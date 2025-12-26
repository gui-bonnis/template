package com.soul.accounting;


import com.soul.fin.accounting.customer.service.CustomerDomainService;
import com.soul.fin.accounting.customer.service.CustomerDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CustomerDomainService customerDomainService() {
        return new CustomerDomainServiceImpl();
    }
    
}
