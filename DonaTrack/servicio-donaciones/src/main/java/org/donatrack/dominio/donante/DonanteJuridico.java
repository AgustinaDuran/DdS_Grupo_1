package org.donatrack.dominio.donante;
import org.donatrack.dominio.usuario.organizacion.Organizacion;

import org.donatrack.dominio.usuario.DatosUsuario;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "donantes_juridicos")
public class DonanteJuridico extends Donante {
    @Enumerated(EnumType.STRING)
    private TipoPersonaJuridica tipoPersonaJuridica;
    private String rubro;

    protected DonanteJuridico() {
    }


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
