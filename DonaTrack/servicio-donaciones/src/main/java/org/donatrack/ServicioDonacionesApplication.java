package org.donatrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServicioDonacionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                ServicioDonacionesApplication.class,
                args
        );
    }
}