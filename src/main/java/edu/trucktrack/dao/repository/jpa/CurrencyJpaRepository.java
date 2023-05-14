package edu.trucktrack.dao.repository.jpa;

import edu.trucktrack.dao.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyJpaRepository extends JpaRepository<CurrencyEntity, Long> {
}
