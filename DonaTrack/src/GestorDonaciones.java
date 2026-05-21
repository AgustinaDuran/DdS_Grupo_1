import java.util.ArrayList;
import java.util.List;

public class GestorDonaciones {
    

    public void agregarDonacion(Donacion donacion){
        List<Donacion> donacionesSegmentadas = segmentarDonacion(donacion);
    
        Deposito deposito = Deposito.GetInstance();
        deposito.agregarDonaciones(donacionesSegmentadas);
    }

    public List<Donacion> segmentarDonacion(Donacion donacionCompleta){
        List<Donacion> donacionesSegmentadas = new ArrayList<>();
        List<ItemBien> bienesDonacion = donacionCompleta.getItemBienes();
        

        for (ItemBien itemBien : bienesDonacion) {
            Subcategoria subcategoriaBien = itemBien.getBien().getSubcategoria();
            Donacion donacionDeSubcategoria = donacionDeSubcategoria(donacionesSegmentadas, subcategoriaBien);
            
            if(donacionesSegmentadas.isEmpty() || donacionDeSubcategoria == null){
                List<ItemBien> primerBienEnLista = new ArrayList<>();
                primerBienEnLista.add(itemBien);
                Donacion donacionNueva = new Donacion(primerBienEnLista);
                donacionesSegmentadas.add(donacionNueva);
            } else{
                donacionDeSubcategoria.agregarItemBien(itemBien);
            }
        }
        
        donacionCompleta = null;
        return donacionesSegmentadas;
    }

    private Donacion donacionDeSubcategoria(List<Donacion> donaciones, Subcategoria subcategoria){
        return donaciones.stream()
            .filter(d -> d.getItemBienes().getBien().getSubcategoria() == subcategoria)
            .findFirst()
            .orElse(null);
    }


}
