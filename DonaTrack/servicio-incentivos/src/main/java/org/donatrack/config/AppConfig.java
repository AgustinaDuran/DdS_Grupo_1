package org.donatrack.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Value("${app.base-url}")
    private String baseUrlConfigurado;

    private static String baseUrl;

    @PostConstruct
    public void Inicializar() {
        baseUrl = baseUrlConfigurado;
    }

    public static String GetBaseUrl() {
        return baseUrl;
    }
}