package org.donatrack.model;

import java.util.List;

public class Transformador extends CategoriaDonante {
    public Transformador() {
      
        Insignia rachaOro = FabricaInsignias.crear(TipoInsignia.RACHA, NivelInsignia.ORO);
        Insignia completitudOro = FabricaInsignias.crear(TipoInsignia.COMPLETITUD, NivelInsignia.ORO);
        Insignia habilOro = FabricaInsignias.crear(TipoInsignia.HABIL_DONADOR, NivelInsignia.ORO);
        Insignia exitosasOro = FabricaInsignias.crear(TipoInsignia.DONACIONES_EXITOSAS, NivelInsignia.ORO);

        this.misiones = List.of(
            new MisionRacha("Completa donaciones durante X meses consecutivos", rachaOro, 12),
            new MisionCompletitud("Donar a X categorías diferentes", completitudOro, 9),
            new MisionHabilDonador("Realizar una donación que supere X cantidad de bienes", habilOro, 20),
            new MisionDonacionesExitosas("Realizar X donaciones exitosas", exitosasOro, 10)
        );
    }

    @Override
    public String getNombre() { return "Transformador"; }

    @Override
    public CategoriaDonante getSiguienteCategoria() {
        return null;
    }
    
}
