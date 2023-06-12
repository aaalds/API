package com.heart.heartapigateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@Service
@EnableDubbo
public class HeartApiGatewayApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HeartApiGatewayApplication.class, args);}
}
