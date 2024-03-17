package edu.hitwh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class ZhaoCiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhaoCiApplication.class, args);
    }

}
