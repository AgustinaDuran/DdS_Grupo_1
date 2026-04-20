public class Direccion{
    private Cliente cliente;
    private String calle1;
    private String calle2;
    private Integer altura;
    private Integer piso;
    private String departamento;
    private Ciudad ciudad;

    public String getDireccion(){
        return calle1 + altura + piso + departamento;
    }
    
    public boolean estaHabilitadaEnvio(){
        return  cliente.estaHabilitado();
    }
}