/* package org.donatrack.repository;

import java.util.List;
import java.util.ArrayList;

public class DonantesRepository {
    
    private static RepositorioDonante instance = null;
    private List<Donante> donantes;

public static RepositorioDonante getInstance() {
        if (instance == null)
            instance = new RepresentanteDonantes();

        return instance;
    }

private DonantesRepository() {
        this.donantes = new ArrayList<>();
    }

public void agregarDonante(Donante donante) {
        this.donantes.add(donante);
    }

public void eliminarDonante(Donante donante) {
        this.donantes.remove(donante);
    }

public List<Donante> getDonantes() {
        return this.donantes;
    }
    
} */