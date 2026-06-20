package org.donatrack.model;

public class FabricaInsignias {

    public static Insignia Crear(TipoInsignia tipo, NivelInsignia nivel) {

        String baseNombre = switch (tipo) {
            case RACHA -> "Racha";
            case COMPLETITUD -> "Completitud";
            case HABIL_DONADOR -> "Hábil Donador";
            case DONACIONES_EXITOSAS -> "Donaciones Exitosas";
        };

        String nombreFinal = baseNombre + " - " + nivel;

        String imagen = "http://localhost:8080/insignias/" + tipo.name().toLowerCase() + "_" +nivel.name().toLowerCase() + ".png";

        return new Insignia(nombreFinal, imagen, nivel);
    }
}