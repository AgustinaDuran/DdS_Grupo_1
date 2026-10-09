package org.donatrack.controller.dto.Donantes;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.donatrack.dominio.donante.Donante;
import org.donatrack.dominio.donante.DonanteJuridico;
import org.donatrack.dominio.donante.DonantePersona;
import org.donatrack.dominio.usuario.organizacion.Organizacion;
import org.donatrack.dominio.usuario.DatosUsuario;
import org.donatrack.dominio.donante.TipoPersonaJuridica;
import org.donatrack.dominio.usuario.persona.Persona;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DonanteDTO {

    private Long id;
    private TipoDonante tipo;

    // Persona humana
    private String nombre;
    private String apellido;
    private Integer edad;
    private String dni;
    private String genero;
    private String direccion;

    // Persona jurídica--
    private TipoPersonaJuridica tipoOrganizacion;
    private String rubro;
    private String razonSocial;
    private String cuit;

    public DonanteDTO(Donante donante) {
        this.id = donante.getId();

        if (donante instanceof DonantePersona donantePersona) {
            this.tipo = TipoDonante.HUMANA;
            
            Persona persona = (Persona) donantePersona.getDatosUsuario();
            if (persona != null) {
                this.nombre = persona.getNombre();
                this.apellido = persona.getApellido();
                this.edad = persona.getEdad();
                this.dni = persona.getDni();
                this.genero = persona.getGenero();
                this.direccion = persona.getDireccion();
            }
        } else if (donante instanceof DonanteJuridico donanteJuridico) {
            this.tipo = TipoDonante.JURIDICA;
            this.tipoOrganizacion = donanteJuridico.getTipoPersonaJuridica();
            this.rubro = donanteJuridico.getRubro();
            Organizacion organizacion = (Organizacion) donanteJuridico.getDatosUsuario();
            if (organizacion != null) {
                this.razonSocial = organizacion.getRazonSocial();
                this.cuit = organizacion.getCuit();
            }
        } else{
            throw new IllegalArgumentException("Tipo de donante desconocido: " + donante.getClass().getName());
        }
    }

    public Long getId() {
        return id;
    }

    public TipoDonante getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Integer getEdad() {
        return edad;
    }

    public String getDni() {
        return dni;
    }

    public String getGenero() {
        return genero;
    }

    public String getDireccion() {
        return direccion;
    }

    public TipoPersonaJuridica getTipoPersonaJuridica() {
        return tipoOrganizacion;
    }

    public String getRubro() {
        return rubro;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public String getCuit() {
        return cuit;
    }
}
