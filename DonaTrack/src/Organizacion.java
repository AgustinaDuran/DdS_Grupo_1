
public class Organizacion{
    private String razonSocial;
    private String cuit;
    private DonanteJuridico rolDonanteJuridico;
    private EntidadBeneficiaria rolEntidadBeneficiaria;
    private List<RepresentanteOrganizacion> representantes;
    private List<Contacto> contactos; 

    public String getRazonSocial() {
    return razonSocial;
}

public Organizacion(){
    
}

public Organizacion(
        String razonSocial,
        String cuit,
        DonanteJuridico rolDonanteJuridico,
        EntidadBeneficiaria rolEntidadBeneficiaria,
        List<RepresentanteOrganizacion> representantes,
        List<Contacto> contactos) {

    this.razonSocial = razonSocial;
    this.cuit = cuit;
    this.rolDonanteJuridico = rolDonanteJuridico;
    this.rolEntidadBeneficiaria = rolEntidadBeneficiaria;
    this.representantes = representantes;
    this.contactos = contactos;
}


public void setRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
}

public String getCuit() {
    return cuit;
}

public void setCuit(String cuit) {
    this.cuit = cuit;
}

public DonanteJuridico getRolDonanteJuridico() {
    return rolDonanteJuridico;
}

public void setRolDonanteJuridico(DonanteJuridico rolDonanteJuridico) {
    this.rolDonanteJuridico = rolDonanteJuridico;
}

public EntidadBeneficiaria getRolEntidadBeneficiaria() {
    return rolEntidadBeneficiaria;
}

public void setRolEntidadBeneficiaria(EntidadBeneficiaria rolEntidadBeneficiaria) {
    this.rolEntidadBeneficiaria = rolEntidadBeneficiaria;
}

public List<RepresentanteOrganizacion> getRepresentantes() {
    return representantes;
}

public void setRepresentantes(List<RepresentanteOrganizacion> representantes) {
    this.representantes = representantes;
}

public List<Contacto> getContactos() {
    return contactos;
}

public void setContactos(List<Contacto> contactos) {
    this.contactos = contactos;
}
    
public void pedir(Necesidad necesidad){
    this.rolEntidadBeneficiaria.registrarNecesidad(necesidad);
}

public void atribuirDonacion(Donacion donacion){
    this.rolDonanteJuridico.agregarDonacion(donacion);
}

void agregarContacto(Contacto nuevoContacto) {
    contactos.add(nuevoContacto);
}

}