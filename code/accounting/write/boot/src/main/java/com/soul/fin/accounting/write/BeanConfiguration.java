package com.soul.fin.accounting.write;


import com.soul.fin.accounting.write.customer.service.CustomerDomainService;
import com.soul.fin.accounting.write.customer.service.CustomerDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CustomerDomainService CustomerDomainService() {
        return new CustomerDomainServiceImpl();
    }

}
