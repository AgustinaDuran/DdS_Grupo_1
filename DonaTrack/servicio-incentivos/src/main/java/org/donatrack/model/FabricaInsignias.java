package org.donatrack.model;

import org.donatrack.config.AppConfig;

public class FabricaInsignias {

    public static Insignia Crear(TipoInsignia tipo, NivelInsignia nivel) {

        String baseNombre = switch (tipo) {
            case RACHA -> "Racha";
            case COMPLETITUD -> "Completitud";
            case HABIL_DONADOR -> "Hábil Donador";
            case DONACIONES_EXITOSAS -> "Donaciones Exitosas";
        };

        String nombreFinal = baseNombre + " - " + nivel;

        String imagen = AppConfig.GetBaseUrl() + "/insignias/" + tipo.name().toLowerCase() + "_" + nivel.name().toLowerCase() + ".png";

        return new Insignia(nombreFinal, imagen, nivel);
    }
}