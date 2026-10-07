package org.donatrack.repository;

import java.util.List;
import org.donatrack.dominio.bien.Bien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BienesRepository extends JpaRepository<Bien, Long> {
    default void agregarBienes(List<Bien> bienes) { saveAll(bienes); }
}
