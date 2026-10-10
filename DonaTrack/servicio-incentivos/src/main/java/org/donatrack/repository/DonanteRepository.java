package org.donatrack.repository;

import org.donatrack.model.DonanteIncentivos;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DonanteRepository extends CrudRepository<DonanteIncentivos, String> {

    //cant usuario cn mas donaciones
        @Query("SELECT COUNT(d) FROM DonanteIncentivos d WHERE d.totalDonaciones > " + "(SELECT du.totalDonaciones FROM DonanteIncentivos du WHERE du.nombreUsuario = :nombreUsuario)")
    Long countDonantesConMasDonaciones(@Param("nombreUsuario") String nombreUsuario);
}