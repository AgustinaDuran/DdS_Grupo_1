package org.donatrack.repository;

import java.util.List;
import org.donatrack.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionesRepository extends JpaRepository<Notificacion, Long> {
    @Query("select n from Notificacion n order by n.fechaEnvio")
    List<Notificacion> findAll();
}
