package org.donatrack.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "insignias")

public class Insignia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String imagen;

    @Enumerated(EnumType.STRING)
    private NivelInsignia nivel;
    
    public Insignia() {}

    public Insignia(String nombre, String imagen, NivelInsignia nivel) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.nivel = nivel;
    }

    public String GetNombre() { return nombre; }

    public String GetImagen() { return imagen; }

    public NivelInsignia GetNivel() { return nivel; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Insignia)) return false;
        Insignia otra = (Insignia) o;
        return Objects.equals(nombre, otra.nombre);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
