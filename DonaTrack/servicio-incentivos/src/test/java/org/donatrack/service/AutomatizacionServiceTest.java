package org.donatrack.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.donatrack.model.Insignia;
import org.donatrack.model.NivelInsignia;
import org.junit.jupiter.api.Test;

class AutomatizacionServiceTest {

    @Test
    void elHitoDeInsigniaIncluyeLaClaveDelDataStoreDeMake() {
        Insignia insignia = new Insignia("Racha - PLATA", "http://host/insignias/racha_plata.png", NivelInsignia.PLATA);

        Map<String, Object> datos = AutomatizacionService.ArmarDatosInsignia(
                "ana", "Completa donaciones durante X meses consecutivos", insignia);

        assertThat(datos).containsEntry("tipo", "INSIGNIA")
                .containsEntry("usuario", "ana")
                .containsEntry("insignia", "Racha - PLATA")
                .containsEntry("clave", "racha_plata")
                .containsEntry("mision", "racha")
                .containsEntry("nivel", "plata")
                .containsEntry("imagenUrl", "http://host/insignias/racha_plata.png");
        assertThat((String) datos.get("texto")).contains("@ana").contains("Racha - PLATA");
    }

    @Test
    void laClaveSacaLasTildesYUsaGuionBajo() {
        Insignia habil = new Insignia("Hábil Donador - BRONCE", "x", NivelInsignia.BRONCE);
        Insignia exitosas = new Insignia("Donaciones Exitosas - ORO", "x", NivelInsignia.ORO);

        Map<String, Object> datosHabil = AutomatizacionService.ArmarDatosInsignia("ana", "d", habil);
        Map<String, Object> datosExitosas = AutomatizacionService.ArmarDatosInsignia("ana", "d", exitosas);

        assertThat(datosHabil).containsEntry("clave", "habil_donador_bronce")
                .containsEntry("mision", "habil_donador")
                .containsEntry("nivel", "bronce");
        assertThat(datosExitosas).containsEntry("clave", "donaciones_exitosas_oro")
                .containsEntry("mision", "donaciones_exitosas")
                .containsEntry("nivel", "oro");
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
