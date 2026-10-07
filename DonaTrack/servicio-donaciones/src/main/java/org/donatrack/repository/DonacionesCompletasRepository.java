package org.donatrack.repository;

import org.donatrack.dominio.donacion.DonacionCompleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonacionesCompletasRepository extends JpaRepository<DonacionCompleta, Long> { }
