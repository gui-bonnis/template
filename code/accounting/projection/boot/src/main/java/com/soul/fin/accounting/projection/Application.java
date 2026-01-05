package com.soul.fin.accounting.projection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableR2dbcRepositories(basePackages = "com.soul.fin.accounting.projection.data")
@SpringBootApplication(scanBasePackages = "com.soul.fin")
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
