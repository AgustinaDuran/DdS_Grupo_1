package org.donatrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DonatrackDonacionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(DonatrackDonacionesApplication.class, args);
    }
}
