package org.donatrack.repository;

import org.donatrack.model.Camion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CamionRepository extends MongoRepository<Camion, String> {
    List<Camion> findByRutaActivaIsNullAndActivoTrue();   // camiones disponibles
    List<Camion> findByRutaActivaIsNull();
    List<Camion> findByRutaActivaIsNotNull();             // camiones en ruta
    List<Camion> findByActivoTrue();
}