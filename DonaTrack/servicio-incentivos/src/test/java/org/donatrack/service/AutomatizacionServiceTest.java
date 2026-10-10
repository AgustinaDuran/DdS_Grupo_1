package org.donatrack.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.donatrack.model.Insignia;
import org.donatrack.model.NivelInsignia;
import org.junit.jupiter.api.Test;

class AutomatizacionServiceTest {

    @Test
    void elHitoDeInsigniaIncluyeNombreNivelEImagen() {
        Insignia insignia = new Insignia("Racha - PLATA", "http://host/insignias/racha_plata.png", NivelInsignia.PLATA);

        Map<String, Object> datos = AutomatizacionService.ArmarDatosInsignia(
                "ana", "Completa donaciones durante X meses consecutivos", insignia);

        assertThat(datos).containsEntry("tipo", "INSIGNIA")
                .containsEntry("usuario", "ana")
                .containsEntry("insignia", "Racha - PLATA")
                .containsEntry("nivel", "PLATA")
                .containsEntry("imagenUrl", "http://host/insignias/racha_plata.png");
        assertThat((String) datos.get("texto")).contains("@ana").contains("Racha - PLATA");
    }

    @Test
    void elAscensoIncluyeLaCategoriaNueva() {
        Map<String, Object> datos = AutomatizacionService.ArmarDatosAscenso("ana", "Sostenedor");

        assertThat(datos).containsEntry("tipo", "ASCENSO")
                .containsEntry("usuario", "ana")
                .containsEntry("categoria", "Sostenedor");
        assertThat((String) datos.get("texto")).contains("@ana").contains("Sostenedor");
    }
}