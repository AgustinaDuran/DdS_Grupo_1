package org.donatrack.repository;

import org.donatrack.model.Entrega;
import org.donatrack.model.EstadoEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> { // entregas no planidicadas noi q salieron
    List<Entrega> findByEstado(EstadoEntrega estado);

    // Para volver a vincular las entregas que devuelve el planificador externo sin el id.
    List<Entrega> findByDonacionIdAndEstado(String donacionId, EstadoEntrega estado);
}