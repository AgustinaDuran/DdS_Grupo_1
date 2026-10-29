package org.donatrack.repository;

import org.donatrack.model.Entrega;
import org.donatrack.model.EstadoEntrega;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntregaRepository extends MongoRepository<Entrega, String> {
    List<Entrega> findByEstado(EstadoEntrega estado);
    List<Entrega> findByDonacionIdAndEstado(String donacionId, EstadoEntrega estado);
}