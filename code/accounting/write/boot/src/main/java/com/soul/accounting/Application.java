package com.soul.accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@EntityScan(basePackages = {"com.soul.fin.accounting.data", "com.soul.fin.accounting.data"})
@SpringBootApplication(scanBasePackages = "com.soul.fin")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
