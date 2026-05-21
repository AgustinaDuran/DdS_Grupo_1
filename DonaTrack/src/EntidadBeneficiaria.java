import java.util.List;

public class EntidadBeneficiaria {
    
    private TipoEntidadBeneficiaria tipoEntidad;
    private String razonSocial;
    private String direccion;
    private Telefono telefono;
    private List<Mail> correosRepresentantes;
    private List<Necesidad> necesidades;

    public void auditarEntidad(){ //para un futuro

    }
    
    public void registrarNecesidad(Necesidad necesidad){
        necesidades.add(necesidad);
    }

    public void confirmarRecepcionDonacion(Necesidad necesidad){ //confirma cuando una necesidad es saldada
        necesidad.marcarComoSaldada();
    }

    public void verEstadoDonaciones(){ //CHEQUEAR

       for (Donacion donacion : donaciones){
            System.out.println(donacion.getDescripcion() + " -> " + donacion.getEstado());
        }
    }

    public void seguirEntregasActivas() { //CHEQUEAR
        for (Donacion donacion : donaciones) {
            if (donacion.getEstado() == EstadoDonacion.ENTREGA_ACTIVA) {
                System.out.println("Entrega activa: "+ donacion.getDescripcion());
            }
        }
    }

    public void visualizarCamiones() {

        //FALTA
    }

    public void recibirNotificacion(String mensaje){
        System.out.println("Notificación: " + mensaje);
    }

    public void cargarFotoDonacion(String foto, Bien bien) {
        bien.agregarFoto(foto);
    }
}

