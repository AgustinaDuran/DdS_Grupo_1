package org.donatrack.repository;

import org.donatrack.model.DonanteIncentivos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonanteRepository extends CrudRepository<DonanteIncentivos, String> {
}