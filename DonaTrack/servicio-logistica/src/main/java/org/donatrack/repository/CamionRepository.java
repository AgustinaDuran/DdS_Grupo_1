package org.donatrack.repository;

import org.donatrack.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CamionRepository extends JpaRepository<Camion, String> {
    List<Camion> findByRutaActivaIsNullAndActivoTrue(); //camiones disp

    List<Camion> findByRutaActivaIsNull(); //camiones sin ruta activa

    List<Camion> findByRutaActivaIsNotNull(); //camiones en ruta

    List<Camion> findByActivoTrue(); //camiones activos
}