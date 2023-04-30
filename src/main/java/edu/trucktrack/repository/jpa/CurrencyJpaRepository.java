package edu.trucktrack.repository.jpa;

import edu.trucktrack.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyJpaRepository extends JpaRepository<CurrencyEntity, Long> {
}
