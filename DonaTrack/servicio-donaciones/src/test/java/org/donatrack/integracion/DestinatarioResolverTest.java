package org.donatrack.integracion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import org.donatrack.dominio.contacto.Contacto;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.donatrack.dominio.entidadBeneficiaria.TipoEntidadBeneficiaria;
import org.donatrack.dominio.usuario.organizacion.Organizacion;
import org.donatrack.dominio.usuario.persona.Persona;
import org.donatrack.integracion.dto.ContactoNotificacion;
import org.donatrack.integracion.dto.NotificacionRequest;
import org.junit.jupiter.api.Test;

class DestinatarioResolverTest {

    private final DestinatarioResolver resolver = new DestinatarioResolver();

    @Test
    void notificaAlRepresentanteDesignadoDeLaEntidad() {
        EntidadBeneficiaria entidad = entidadCon(
                representante("Laura", "Gomez", "MAIL", "laura@comedor.org"));

        NotificacionRequest request = resolver.paraEntidad(entidad, "Se te asignó una donación.");

        assertThat(request.getNombreDestinatario()).isEqualTo("Comedor Escobar Sonrisas");
        assertThat(request.getMensaje()).isEqualTo("Se te asignó una donación.");
        assertThat(request.getContactos())
                .extracting(ContactoNotificacion::getTipo, ContactoNotificacion::getValor)
                .containsExactly(tuple("MAIL", "laura@comedor.org"));
    }

    @Test
    void notificaATodosLosRepresentantes() {
        EntidadBeneficiaria entidad = entidadCon(
                representante("Laura", "Gomez", "MAIL", "laura@comedor.org"),
                representante("Diego", "Ruiz", "MAIL", "diego@comedor.org"));

        NotificacionRequest request = resolver.paraEntidad(entidad, "Inició la ruta.");

        assertThat(request.getContactos())
                .extracting(ContactoNotificacion::getValor)
                .containsExactly("laura@comedor.org", "diego@comedor.org");
    }

    @Test
    void noRepiteUnContactoCompartidoPorDosRepresentantes() {
        EntidadBeneficiaria entidad = entidadCon(
                representante("Laura", "Gomez", "MAIL", "info@comedor.org"),
                representante("Diego", "Ruiz", "MAIL", "INFO@comedor.org"));

        NotificacionRequest request = resolver.paraEntidad(entidad, "La donación fue entregada.");

        assertThat(request.getContactos()).hasSize(1);
    }

    @Test
    void sinRepresentantesDevuelveContactosVaciosSinFallar() {
        EntidadBeneficiaria entidad = entidadCon();

        NotificacionRequest request = resolver.paraEntidad(entidad, "La entrega no se concretó.");

        assertThat(request.getContactos()).isEmpty();
        assertThat(request.getNombreDestinatario()).isEqualTo("Comedor Escobar Sonrisas");
    }

    @Test
    void ignoraRepresentantesSinContactoCargado() {
        EntidadBeneficiaria entidad = entidadCon(
                new Persona("Sin", "Contacto", null),
                representante("Laura", "Gomez", "MAIL", "laura@comedor.org"));

        NotificacionRequest request = resolver.paraEntidad(entidad, "Se te asignó una donación.");

        assertThat(request.getContactos())
                .extracting(ContactoNotificacion::getValor)
                .containsExactly("laura@comedor.org");
    }

    private EntidadBeneficiaria entidadCon(Persona... representantes) {
        Organizacion organizacion = new Organizacion("Comedor Escobar Sonrisas", "30-99999999-9");
        for (Persona representante : representantes) {
            organizacion.darAltaRepresentante(representante);
        }
        return new EntidadBeneficiaria(
                TipoEntidadBeneficiaria.COMEDOR, "Av. Siempreviva 742", organizacion);
    }

    private Persona representante(String nombre, String apellido, String medio, String valor) {
        return new Persona(nombre, apellido, new Contacto(medio, valor));
    }
}
