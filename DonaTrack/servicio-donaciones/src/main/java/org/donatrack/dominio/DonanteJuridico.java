package org.donatrack.dominio;

/* import java.util.ArrayList;
import java.util.List; */

public class DonanteJuridico extends Donante {
    private TipoOrganizacion tipoOrganizacion;
    private String rubro;

    public DonanteJuridico(TipoOrganizacion tipoOrganizacion, String rubro) {
        this.tipoOrganizacion = tipoOrganizacion;
        this.rubro = rubro;
    }

    public TipoOrganizacion getTipoOrganizacion() {
        return this.tipoOrganizacion;
    }

    public String getRubro() {
        return this.rubro;
    }
}
