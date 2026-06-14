package org.donatrack.donaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApp
publiic class ServicioDonacionesApp {

    public static void main(String[] args) {
        SpringApplication.run(
                ServicioDonacionesApplication.class,
                args
        );
    }
}