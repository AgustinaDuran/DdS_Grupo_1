package org.donatrack.model;

import java.util.List;

public class Transformador extends CategoriaDonante {
    public Transformador() {
      
        Insignia insigniaRachaOro = new Insignia("Racha - Oro", img);
        Insignia insigniaCompletitudOro = new Insignia("Completitud - Oro", img);
        Insignia insigniaHabilDonadorOro = new Insignia("Habil Donador - Oro", img);
        Insignia insigniaMisionDonacionesExitosasOro = new Insignia("Donaciones Exitosas - Oro", img);

        this.misiones = List.of(
            new MisionRacha("Realizar una donacion durante X meses seguidos", 12, insigniaRachaOro),
            new MisionCompletitud("Realizar donaciones de X categorias distintas", 9, insigniaCompletitudOro),
            new MisionHabilDonador("Donación que supere X cantidad de bienes", 20, insigniaHabilDonadorOro),
            new MisionDonacionesExitosas("Lograr X donaciones que sean recibidas exitosamente por una entidad beneficiaria", 10, insigniaMisionDonacionesExitosasOro)
        );
    }

    @Override
    public String getNombre() { return "Transformador"; }

    @Override
    public CategoriaDonante getSiguienteCategoria() {
        return null;
    }
    
}
