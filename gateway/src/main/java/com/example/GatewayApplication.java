package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GatewayApplication {
//    org.springframework.cloud.gateway.filter.WeightCalculatorWebFilter
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
