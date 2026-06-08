package org.donatrack.model;

import java.util.ArrayList;
import java.util.List;

public class DonanteJuridico extends Donante {
    private TipoPersonaJuridica tipoPersonaJuridica;
    private String rubro;

    public DonanteJuridico(TipoPersonaJuridica tipoPersonaJuridica, String rubro) {
        this.tipoPersonaJuridica = tipoPersonaJuridica;
        this.rubro = rubro;
    }

    public TipoPersonaJuridica getTipoPersonaJuridica() {
        return this.tipoPersonaJuridica;
    }

    public String getRubro() {
        return this.rubro;
    }
}
