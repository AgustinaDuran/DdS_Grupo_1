package org.donatrack.dominio.organizacion;

public class RepresentanteOrganizacion {
    private String nombre;
    private Integer nroDocumento;

    public RepresentanteOrganizacion(String nombre, Integer nroDocumento) {
        this.nombre = nombre;
        this.nroDocumento = nroDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNroDocumento() {
        return nroDocumento;
    }

    public void setDocumento(Integer nroDocumento) {
        this.nroDocumento = nroDocumento;
    }
    
}
