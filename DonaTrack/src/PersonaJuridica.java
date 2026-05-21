import java.util.ArrayList;
import java.util.List;

public class PersonaJuridica extends Donante {
    private String razonSocial;
    private TipoOrganizacion tipoOrganizacion;
    private String rubro;
    private List<RepresentanteOrganizacion> representantes = new ArrayList<>();

    public PersonaJuridica(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
}
