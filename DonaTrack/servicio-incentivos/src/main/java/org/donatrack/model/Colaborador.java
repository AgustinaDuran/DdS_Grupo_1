package org.donatrack.model;

import java.util.List;

public class Colaborador extends CategoriaDonante {
    public Colaborador() {
    
        Insignia insigniaRachaBronce = new Insignia("Racha - Bronce", img);
        Insignia insigniaCompletitudBronce = new Insignia("Completitud - Bronce", img);
        Insignia insigniaHabilDonadorBronce = new Insignia("Habil Donador - Bronce", img);
        Insignia insigniaMisionDonacionesExitosasBronce = new Insignia("Donaciones Exitosas - Bronce", img);

        this.misiones = List.of(
            new MisionRacha("Realizar una donacion durante X meses seguidos", 3, insigniaRachaBronce),
            new MisionCompletitud("Realizar donaciones de X categorias distintas", 3, insigniaCompletitudBronce),
            new MisionHabilDonador("Donación que supere X cantidad de bienes", 2, insigniaHabilDonadorBronce),
            new MisionDonacionesExitosas("Lograr X donaciones que sean recibidas exitosamente por una entidad beneficiaria", 2, insigniaMisionDonacionesExitosasBronce)
        );
    }

    @Override
    public String getNombre() { return "Colaborador"; }

    @Override
    public CategoriaDonante getSiguienteCategoria() {
        return new Sostenedor();
    }
    
}
