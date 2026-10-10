package org.donatrack.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.donatrack.config.AppConfig;
import org.donatrack.repository.InsigniaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FabricaInsignias {

    private static final Logger log = LoggerFactory.getLogger(FabricaInsignias.class);

    private static FabricaInsignias instancia;

    private final InsigniaRepository insigniaRepository;

    private final Map<String, Insignia> cache = new ConcurrentHashMap<>();

    public FabricaInsignias(InsigniaRepository insigniaRepository) {
        this.insigniaRepository = insigniaRepository;
        instancia = this;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void precargarInsignias() {
        try {
            for (TipoInsignia tipo : TipoInsignia.values()) {
                for (NivelInsignia nivel : NivelInsignia.values()) {
                    Crear(tipo, nivel);
                }
            }
        } catch (Exception e) {
            log.warn("No se pudo precargar el catálogo de insignias: {}", e.getMessage());
        }
    }

    public static Insignia Crear(TipoInsignia tipo, NivelInsignia nivel) {
        if (instancia == null) {
            return construirInsignia(tipo, nivel);
        }
        return instancia.crearInsignia(tipo, nivel);
    }

    private Insignia crearInsignia(TipoInsignia tipo, NivelInsignia nivel) {
        return cache.computeIfAbsent(nombreFinal(tipo, nivel), nombre -> insigniaRepository.findByNombre(nombre).orElseGet(() -> insigniaRepository.save(construirInsignia(tipo, nivel))));
    }

    private static Insignia construirInsignia(TipoInsignia tipo, NivelInsignia nivel) {
        String imagen = AppConfig.GetBaseUrl() + "/insignias/" + tipo.name().toLowerCase() + "_" + nivel.name().toLowerCase() + ".png";
        return new Insignia(nombreFinal(tipo, nivel), imagen, nivel);
    }

    private static String nombreFinal(TipoInsignia tipo, NivelInsignia nivel) {
        String baseNombre = switch (tipo) {
            case RACHA -> "Racha";
            case COMPLETITUD -> "Completitud";
            case HABIL_DONADOR -> "Hábil Donador";
            case DONACIONES_EXITOSAS -> "Donaciones Exitosas";
        };
        return baseNombre + " - " + nivel;
    }
}
