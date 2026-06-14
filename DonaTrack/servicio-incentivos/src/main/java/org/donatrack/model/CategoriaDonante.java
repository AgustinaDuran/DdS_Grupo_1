package org.donatrack.model;
import java.util.List;

public abstract class CategoriaDonante {  
    protected List<Mision> misiones;

    public abstract String getNombre();

    public abstract CategoriaDonante getSiguienteCategoria();

    public boolean completoTodasLasMisiones(DonanteIncentivos donante) {
        for (Mision mision : misiones) {
            if (!mision.estaCumplidaPor(donante)) {
                return false; 
            }
        }
        return true;
    }

    public List<Mision> getMisionesDelNivel() {
        return misiones;
    }
}