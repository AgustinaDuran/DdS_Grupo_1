package org.donatrack.dominio.bien;

public class ItemBien {
    
    private Bien bien;
    private Integer cantidad;

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
