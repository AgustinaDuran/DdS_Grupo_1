package org.donatrack.model;

import java.util.List;

public class Sostenedor extends CategoriaDonante {
    public Sostenedor() {
        Insignia rachaPlata = FabricaInsignias.crear(TipoInsignia.RACHA, NivelInsignia.PLATA);
        Insignia completitudPlata = FabricaInsignias.crear(TipoInsignia.COMPLETITUD, NivelInsignia.PLATA);
        Insignia habilPlata = FabricaInsignias.crear(TipoInsignia.HABIL_DONADOR, NivelInsignia.PLATA);
        Insignia exitosasPlata = FabricaInsignias.crear(TipoInsignia.DONACIONES_EXITOSAS, NivelInsignia.PLATA);

        this.misiones = List.of(
            new MisionRacha("Completa donaciones durante X meses consecutivos", rachaPlata, 6),
            new MisionCompletitud("Donar a X categorías diferentes", completitudPlata, 6),
            new MisionHabilDonador("Realizar una donación que supere X cantidad de bienes", habilPlata, 10),
            new MisionDonacionesExitosas("Realizar X donaciones exitosas", exitosasPlata, 5)
        );
    }

    @Override
    public String getNombre() { return "Sostenedor"; }

    @Override
    public CategoriaDonante getSiguienteCategoria() {
        return new Transformador();
    }
}
