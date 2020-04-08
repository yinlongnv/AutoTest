package com.dadalong.autotest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@ComponentScan("com.dadalong")
public class AutotestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutotestApplication.class, args);
        System.out.println("run success!!!");
    }

}
