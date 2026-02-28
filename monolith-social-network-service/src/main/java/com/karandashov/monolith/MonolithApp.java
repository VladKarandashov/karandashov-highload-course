package com.karandashov.monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.karandashov.monolith")
public class MonolithApp {

    public static void main(String[] args) {
        SpringApplication.run(MonolithApp.class, args);
    }
}
