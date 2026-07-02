package org.donatrack.dominio.donante;
import org.donatrack.dominio.contacto.*;
import org.donatrack.dominio.usuario.persona.Persona;
import org.donatrack.dominio.usuario.DatosUsuario;

import java.util.ArrayList;
import java.util.List;

public class DonantePersona extends Donante{
    EstadoActividad estadoActividad;

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
