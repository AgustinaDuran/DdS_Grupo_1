package org.donatrack.repository;
import org.donatrack.dominio.Donacion;

import java.util.ArrayList;
import java.util.List;

public class RepositoryDonaciones { // singleton

    private static RepositoryDonaciones instance = null;
    private List<Donacion> donaciones; // a reemplazar por un


    private RepositoryDonaciones() { //constructor privado
        donaciones = new ArrayList<>();
    }

    public static RepositoryDonaciones getInstance() {
        if (instance == null)
            instance = new RepositoryDonaciones();

        return instance;
    }

    public void agregarDonaciones(List<Donacion> donaciones){
        this.donaciones.addAll(donaciones);
    }

    public List<Donacion> getDonaciones(){
        return donaciones;
    }

}
