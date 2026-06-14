package org.donatrack.model;

import java.util.List;

public class Sostenedor extends CategoriaDonante {
    public Sostenedor() {
        Insignia insigniaRachaPlata = new Insignia("Racha - Plata", img);
        Insignia insigniaCompletitudPlata = new Insignia("Completitud - Plata", img);
        Insignia insigniaHabilDonadorPlata = new Insignia("Habil Donador - Plata", img);
        Insignia insigniaMisionDonacionesExitosasPlata = new Insignia("Donaciones Exitosas - Plata", img);

        this.misiones = List.of(
            new MisionRacha("Realizar una donacion durante X meses seguidos", 6, insigniaRachaPlata),
            new MisionCompletitud("Realizar donaciones de X categorias distintas", 6, insigniaCompletitudPlata),
            new MisionHabilDonador("Donación que supere X cantidad de bienes", 10, insigniaHabilDonadorPlata),
            new MisionDonacionesExitosas("Lograr X donaciones que sean recibidas exitosamente por una entidad beneficiaria", 5, insigniaMisionDonacionesExitosasPlata)
        );
    }

    @Override
    public String getNombre() { return "Sostenedor"; }

    @Override
    public CategoriaDonante getSiguienteCategoria() {
        return new Transformador();
    }
}
