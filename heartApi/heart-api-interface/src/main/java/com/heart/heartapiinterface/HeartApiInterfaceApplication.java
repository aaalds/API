package com.heart.heartapiinterface;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
public class HeartApiInterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeartApiInterfaceApplication.class, args);
    }

}
