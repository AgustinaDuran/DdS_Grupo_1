package org.donatrack.repository;

import java.util.List;
import java.util.Optional;
import org.donatrack.dominio.entidadBeneficiaria.EntidadBeneficiaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntidadesBeneficiariasRepository extends JpaRepository<EntidadBeneficiaria, Long> {
    default void agregarEntidades(List<EntidadBeneficiaria> entidades) { saveAll(entidades); }
    Optional<EntidadBeneficiaria> findByKeycloakUserId(String keycloakUserId);
}
