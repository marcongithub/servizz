package com.servizz.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@ComponentScan(basePackages = {"com.servizz"})
@Profile("default")
public class Application implements CommandLineRunner {


    @Value("${application.environment}")
    private String environment;

    @Value("${application.name}")
    private String appName;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    public void run(String... args){
        System.out.println();
        System.out.println("######################################################################");
        System.out.println("###### " + appName + " application started in " + environment + " mode. ##############");
        System.out.println("######################################################################");
        System.out.println();
    }


}
