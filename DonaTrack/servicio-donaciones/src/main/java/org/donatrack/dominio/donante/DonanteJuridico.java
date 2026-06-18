package org.donatrack.dominio.donante;
import org.donatrack.dominio.organizacion.TipoOrganizacion;
import org.donatrack.dominio.organizacion.Organizacion;

/* import java.util.ArrayList;
import java.util.List; */

public class DonanteJuridico extends Donante {
    private TipoOrganizacion tipoOrganizacion;
    private String rubro;
    private Organizacion organizacion;

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

    public Organizacion getOrganizacion() {
        return this.organizacion;
    }
}
