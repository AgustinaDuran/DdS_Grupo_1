package org.donatrack.dominio.bien;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class ItemBien {

    @ManyToOne
    @JoinColumn(name = "bien_id")
    private Bien bien;
    private Integer cantidad;

    protected ItemBien() {
    }

    public ItemBien(Bien bien, Integer cantidad) {
        this.bien = bien;
        this.cantidad = cantidad;
    }

    public Bien getBien(){
        return bien;
    }
    public int getCantidad(){
        return cantidad;
    }
}
