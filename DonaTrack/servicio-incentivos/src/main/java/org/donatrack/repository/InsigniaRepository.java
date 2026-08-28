package org.donatrack.repository;

import org.donatrack.model.Insignia;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InsigniaRepository extends CrudRepository<Insignia, Long> {
    Optional<Insignia> findByNombre(String nombre);
}