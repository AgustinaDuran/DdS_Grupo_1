package org.donatrack.model;

import org.donatrack.config.AppConfig;
import org.donatrack.repository.InsigniaRepository;
import org.springframework.stereotype.Component;

@Component
public class FabricaInsignias {

    private final InsigniaRepository insigniaRepository;

    public FabricaInsignias(InsigniaRepository insigniaRepository) {
        this.insigniaRepository = insigniaRepository;
    }

    public Insignia Crear(TipoInsignia tipo, NivelInsignia nivel) {

        String baseNombre = switch (tipo) {
            case RACHA -> "Racha";
            case COMPLETITUD -> "Completitud";
            case HABIL_DONADOR -> "Hábil Donador";
            case DONACIONES_EXITOSAS -> "Donaciones Exitosas";
        };

        String nombreFinal = baseNombre + " - " + nivel;

        return insigniaRepository.findByNombre(nombreFinal).orElseGet(() -> {
            String imagen = AppConfig.GetBaseUrl() + "/insignias/" + tipo.name().toLowerCase() + "_" + nivel.name().toLowerCase() + ".png";
            Insignia nueva = new Insignia(nombreFinal, imagen, nivel);
            return insigniaRepository.save(nueva);
        });
    }
}