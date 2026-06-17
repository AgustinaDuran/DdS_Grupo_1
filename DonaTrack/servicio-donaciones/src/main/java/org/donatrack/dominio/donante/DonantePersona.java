package org.donatrack.dominio.donante;
import org.donatrack.dominio.contacto.*;


import java.util.ArrayList;
import java.util.List;

public class DonantePersona extends Donante{
    private String direccion;
    protected List<Contacto> contactos = new ArrayList<>();
    private Contacto contactoPredeterminado;

    public DonantePersona(String direccion, Mail mail) {
        this.direccion = direccion;
        this.contactos.add(mail);
        this.contactoPredeterminado = mail;
    }

    public void setContactoPredeterminado(Contacto contacto){
        if(!getContactos().contains(contacto)){
            throw new RuntimeException("El contacto no pertenece a la persona");
        }

        this.contactoPredeterminado = contacto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Contacto getContactoPredeterminado() {
        return contactoPredeterminado;
    }

    public void agregarContacto(Contacto nuevoContacto) {
        contactos.add(nuevoContacto);
    }

    public List<Contacto> getContactos(){
        return contactos;
    }
}
