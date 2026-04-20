import java.util.List;

public class Cliente{
    private String nombre;
    private String apellido;
    private String email;
    private List<Direccion> direcciones;
    private List<Carrito> carritos;
    private List<Tarjeta> tarjetas;
    private boolean preferencial;

    public boolean esPreferencial(){
        return preferencial;
    }

    public float getMontoDeuda(){
        float montoDeudaTotal = 0;
        
        for (Carrito carrito : carritos) {
            montoDeudaTotal += carrito.getMontoDeuda();
        }
        return montoDeudaTotal;
    }

    public boolean estaHabilitado(){ //se fija si la deuda es mayor a un monto arbitrario
        return this.getMontoDeuda() < 10000; //monto arbitrario 10000, se asume deuda viene como num positivo
    }

    //getters y setters
/* 
    public String getNombre() {
    return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }

    public List<Carrito> getCarritos() {
        return carritos;
    }

    public void setCarritos(List<Carrito> carritos) {
        this.carritos = carritos;
    }

    public List<Tarjeta> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<Tarjeta> tarjetas) {
        this.tarjetas = tarjetas;
    }

    

    public void setEsPreferencial(boolean esPreferencial) {
        this.esPreferencial = esPreferencial;
    }
 */
}