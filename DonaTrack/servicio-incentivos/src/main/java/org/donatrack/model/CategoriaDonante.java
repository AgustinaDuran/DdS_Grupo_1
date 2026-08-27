package org.donatrack.model;
import java.util.List;

public abstract class CategoriaDonante {  
    protected List<Mision> misiones;
    protected FabricaInsignias fabricaInsignias;

    protected CategoriaDonante(FabricaInsignias fabricaInsignias) {  // ← nuevo constructor
        this.fabricaInsignias = fabricaInsignias;
    }

    public abstract String GetNombre();

    public abstract CategoriaDonante GetSiguienteCategoria();

    public Boolean CompletoTodasLasMisiones(DonanteIncentivos donante) {
        for (Mision mision : misiones) {
            if (!mision.EstaCumplidaPor(donante)) {
                return false; 
            }
        }
        return true;
    }

    public List<Mision> GetMisionesDelNivel() {
        return misiones;
    }
}