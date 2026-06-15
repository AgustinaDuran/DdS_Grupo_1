package org.donatrack.model;

public class Insignia {
    private String nombre;
    private String imagen;
    private NivelInsignia nivel;
    
    public Insignia(String nombre, String imagen, NivelInsignia nivel) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.nivel = nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public NivelInsignia getNivel() {
        return nivel;
    }
}
