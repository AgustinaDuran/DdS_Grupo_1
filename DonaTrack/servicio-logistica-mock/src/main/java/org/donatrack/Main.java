package org.donatrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Proveedor de logística externo simulado. Representa a otro servicio que cumple el mismo
 * objetivo que nuestro Servicio de Logística pero con una API propia, para que el broker de
 * Donaciones tenga entre qué elegir. Guarda los envíos en memoria.
 */
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
