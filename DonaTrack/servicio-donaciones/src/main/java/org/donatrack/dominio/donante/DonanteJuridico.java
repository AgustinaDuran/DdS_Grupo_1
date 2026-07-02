package org.donatrack.dominio.donante;
import org.donatrack.dominio.usuario.organizacion.Organizacion;

import org.donatrack.dominio.usuario.DatosUsuario;
/* import java.util.ArrayList;
import java.util.List; */

public class DonanteJuridico extends Donante {
    private TipoPersonaJuridica tipoPersonaJuridica;
    private String rubro;


    public DonanteJuridico(DatosUsuario datosUsuario, TipoPersonaJuridica tipoPersonaJuridica, String rubro) {
        super(datosUsuario);
        this.tipoPersonaJuridica = tipoPersonaJuridica;
        this.rubro = rubro;
    }

    public TipoPersonaJuridica getTipoPersonaJuridica() {
        return this.tipoPersonaJuridica;
    }

    public void setTipoPersonaJuridica(TipoPersonaJuridica tipoPersonaJuridica) {
        this.tipoPersonaJuridica = tipoPersonaJuridica;
    }

    public String getRubro() {
        return this.rubro;
    }

}
