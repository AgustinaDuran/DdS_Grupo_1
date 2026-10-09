package org.donatrack.repository;

import org.donatrack.model.RankingMensual;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingRepository extends CrudRepository<RankingMensual, String> {
    
}