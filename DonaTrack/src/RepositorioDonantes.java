public class RepositorioDonantes {
    
    private static RepositorioDonantes instance = null;
    private List<Donante> donantes;

public static RepositorioDonantes getInstance() {
        if (instance == null)
            instance = new RepresentanteDonantes();

        return instance;
    }

private RepositorioDonantes() {
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
    
}