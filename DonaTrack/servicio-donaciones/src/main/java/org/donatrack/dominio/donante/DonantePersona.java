package org.donatrack.dominio.donante;
import org.donatrack.dominio.contacto.*;
import org.donatrack.dominio.usuario.persona.Persona;
import org.donatrack.dominio.usuario.DatosUsuario;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "donantes_persona")
public class DonantePersona extends Donante{
    @Enumerated(EnumType.STRING)
    EstadoActividad estadoActividad;

    protected DonantePersona() {
    }

    public DonantePersona(DatosUsuario datosUsuario) {
        super(datosUsuario);
        this.estadoActividad = EstadoActividad.ACTIVO;
    }

    public EstadoActividad getEstadoActividad() {
        return estadoActividad;
    }

    public void setEstadoActividad(EstadoActividad estadoActividad) {
        this.estadoActividad = estadoActividad;
    }
}
