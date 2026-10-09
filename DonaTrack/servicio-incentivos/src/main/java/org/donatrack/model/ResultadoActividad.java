package org.donatrack.model;

import java.util.List;

public class ResultadoActividad {
    private final List<Mision> misionesCumplidas;
    private final String nuevaCategoria; // null si no subió

    public ResultadoActividad(List<Mision> misionesCumplidas, String nuevaCategoria) {
        this.misionesCumplidas = misionesCumplidas;
        this.nuevaCategoria = nuevaCategoria;
    }

    public List<Mision> GetMisionesCumplidas() { return misionesCumplidas; }
    public String GetNuevaCategoria() { return nuevaCategoria; }
}