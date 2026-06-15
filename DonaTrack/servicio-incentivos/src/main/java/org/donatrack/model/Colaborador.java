package org.donatrack.model;

import java.util.List;

public class Colaborador extends CategoriaDonante {
    public Colaborador() {
    
        Insignia rachaBronce = FabricaInsignias.crear(TipoInsignia.RACHA, NivelInsignia.BRONCE);
        Insignia completitudBronce = FabricaInsignias.crear(TipoInsignia.COMPLETITUD, NivelInsignia.BRONCE);
        Insignia habilBronce = FabricaInsignias.crear(TipoInsignia.HABIL_DONADOR, NivelInsignia.BRONCE);
        Insignia exitosasBronce = FabricaInsignias.crear(TipoInsignia.DONACIONES_EXITOSAS, NivelInsignia.BRONCE);

        this.misiones = List.of(
            new MisionRacha("Completa donaciones durante X meses consecutivos", rachaBronce, 3),
            new MisionCompletitud("Donar a X categorías diferentes", completitudBronce, 3),
            new MisionHabilDonador("Realizar una donación que supere X cantidad de bienes", habilBronce, 2),
            new MisionDonacionesExitosas("Realizar X donaciones exitosas", exitosasBronce, 2)
        );
    }

    @Override
    public String getNombre() { return "Colaborador"; }

    @Override
    public CategoriaDonante getSiguienteCategoria() {
        return new Sostenedor();
    }
    
}
