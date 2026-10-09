package org.donatrack.repository;

import java.util.List;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntidadesBeneficiariasRepository extends JpaRepository<EntidadBeneficiaria, Long> {
    default void agregarEntidades(List<EntidadBeneficiaria> entidades) { saveAll(entidades); }
}
