package com.soul.fin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestRunner implements CommandLineRunner {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String bar;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(String... strings) throws Exception {
        logger.info("Foo from @Value: {}", bar);
        logger.info("Foo from System.getenv(): {}", System.getenv("spring.security.oauth2.resourceserver.jwt.issuer-uri")); // Same output as line above
    }
}
