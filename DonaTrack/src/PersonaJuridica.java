import java.util.ArrayList;
import java.util.List;

public class PersonaJuridica extends Donante {
    private String razonSocial;
    private TipoOrganizacion tipoOrganizacion;
    private String rubro;
    private String cuit;
    private List<RepresentanteOrganizacion> representantes = new ArrayList<>();

    public PersonaJuridica(String razonSocial, TipoOrganizacion tipoOrganizacion, String rubro, String cuit, List<RepresentanteOrganizacion> representantes) {
        this.razonSocial = razonSocial;
        this.tipoOrganizacion = tipoOrganizacion;
        this.rubro = rubro;
        this.cuit = cuit;
        this.representantes=representantes;
    }

    public PersonaJuridica(String razonSocial, String cuit, Mail mail, Telefono telefono) {
        this.razonSocial = razonSocial;
        this.contactos.add(mail);
        this.contactos.add(telefono);
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRazonSocial() {
        return this.razonSocial;
    }

    public TipoOrganizacion getTipoOrganizacion() {
        return this.tipoOrganizacion;
    }

    public String getRubro() {
        return this.rubro;
    }

    public String getCuit() {
        return this.cuit;
    }

    public List<RepresentanteOrganizacion> getRepresentantes() {
        return this.representantes;
    }
}
