package org.donatrack.repository;
import org.donatrack.dominio.donacion.Donacion;

import java.util.ArrayList;
import java.util.List;



import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;

public class EntidadesBeneficiariasRepository { // singleton

    private static EntidadesBeneficiariasRepository instance = null;
    private List<EntidadBeneficiaria> entidades;

    private EntidadesBeneficiariasRepository() { // constructor privado
        this.entidades = new ArrayList<>();
    }

    public static EntidadesBeneficiariasRepository getInstance() {
        if (instance == null) {
            instance = new EntidadesBeneficiariasRepository();
        }
        return instance;
    }

    public void agregarEntidad(EntidadBeneficiaria entidad) {
        this.entidades.add(entidad);
    }

    public void eliminarEntidad(EntidadBeneficiaria entidad) {
        this.entidades.remove(entidad);
    }

    public List<EntidadBeneficiaria> getEntidades() {
        return this.entidades;
    }
}
