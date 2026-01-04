package com.soul.fin.accounting.event_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

//@EnableR2dbcRepositories(basePackages = "com.soul.fin.accounting.read.data")
@SpringBootApplication(scanBasePackages = "com.soul.fin")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
