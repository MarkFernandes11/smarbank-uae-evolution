package com.bank.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The main entry point for the Banking Wallet API.
 * * @SpringBootApplication is a convenience annotation that adds:
 * 1. @Configuration: Tags the class as a source of bean definitions.
 * 2. @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings.
 * 3. @ComponentScan: Tells Spring to look for other components, configurations, and services in the package.
 */
@SpringBootApplication
@EnableTransactionManagement // Ensures your ACID logic works with @Transactional
public class BankingWalletApplication {

    public static void main(String[] args) {
        // This line launches the embedded Tomcat server (default port 8080)
        SpringApplication.run(BankingWalletApplication.class, args);

        System.out.println("Banking Wallet API is running smoothly");
    }
}