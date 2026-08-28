package org.donatrack.model;

import org.donatrack.config.AppConfig;
import org.donatrack.repository.InsigniaRepository;
import org.springframework.stereotype.Component;

@Component
public class FabricaInsignias {

    private static FabricaInsignias instancia;

    private final InsigniaRepository insigniaRepository;

    public FabricaInsignias(InsigniaRepository insigniaRepository) {
        this.insigniaRepository = insigniaRepository;
        instancia = this;
    }

    public static Insignia Crear(TipoInsignia tipo, NivelInsignia nivel) {
        if (instancia == null) {
            return construirInsignia(tipo, nivel);
        }
        return instancia.crearInsignia(tipo, nivel);
    }

    private Insignia crearInsignia(TipoInsignia tipo, NivelInsignia nivel) {
        String nombreFinal = nombreFinal(tipo, nivel);
        return insigniaRepository.findByNombre(nombreFinal)
                .orElseGet(() -> insigniaRepository.save(construirInsignia(tipo, nivel)));
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
