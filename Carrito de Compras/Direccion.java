public class Direccion {

    private Cliente cliente;
    private String latitud;
    private String longitud;
    private String calle1;
    private String calle2;
    private Integer altura;
    private Integer piso;
    private String departamento;
    private Ciudad ciudad;

    public String getDireccion() {

        return calle1 + altura + piso + departamento;
    }

    public String getCallesDireccion() {

        return calle1 + calle2;
    }
    
    public Ciudad getCiudad() {

        return ciudad;
    }

    public String getLatitud() {

        return latitud;
    }

    public String getLongitud() {

        return longitud;
    }

    public boolean estaHabilitadaEnvio() {

        return cliente.estaHabilitado();
    }
}