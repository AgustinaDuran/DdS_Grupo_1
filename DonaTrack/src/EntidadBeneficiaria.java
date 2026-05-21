import java.util.List;

public class EntidadBeneficiaria {
    
    private TipoEntidadBeneficiaria tipoEntidad;
    private String razonSocial;
    private String direccion;
    private Telefono telefono;
    private List<Mail> correosRepresentantes;
    private List<Necesidad> necesidades;
    
    public TipoEntidadBeneficiaria getTipoEntidad() { 
        return tipoEntidad; }

    public String getRazonSocial() { 
        return razonSocial; }

    public String getDireccion() { 
        return direccion; }

    public Telefono getTelefono() { 
        return telefono; }
    
    public List<Mail> getCorreosRepresentantes() { 
        return correosRepresentantes; }

    public List<Necesidad> getNecesidades() { 
        return necesidades; }

    public void auditarEntidad(){ //para un futuro

    }
    
    public void registrarNecesidad(Necesidad necesidad){
        necesidades.add(necesidad);
    }

    public void confirmarRecepcionDonacion(Necesidad necesidad){ //confirma cuando una necesidad es saldada
        necesidad.marcarComoSaldada();
    }

    public void verEstadoDonaciones(List<Donacion> donaciones){

       for (Donacion donacion : donaciones){
            System.out.println(donacion.getDescripcion() + " -> " + donacion.getEstadoDonacion());
        }
    }

    public void seguirEntregasActivas(List<Donacion> donaciones) {
        for (Donacion donacion : donaciones) {
            if (donacion.getEstadoDonacion() == EstadoDonacion.ENTREGA_ACTIVA) {
                System.out.println("Entrega activa: "+ donacion.getDescripcion());
            }
        }
    }

    public void recibirNotificacion(String mensaje){
        System.out.println("Notificación: " + mensaje);
    }


    public void cargarFotosDonacion(String foto, Bien bien){
    bien.agregarFoto(foto);
    }
    


}

