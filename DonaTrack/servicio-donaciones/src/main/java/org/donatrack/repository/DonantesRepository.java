package org.donatrack.repository;

import java.util.List;
import org.donatrack.dominio.donante.Donante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonantesRepository extends JpaRepository<Donante, Long> {
    default void agregarDonantes(List<Donante> donantes) { saveAll(donantes); }
}
