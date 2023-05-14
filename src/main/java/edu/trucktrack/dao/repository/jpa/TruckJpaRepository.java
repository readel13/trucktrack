package edu.trucktrack.dao.repository.jpa;

import edu.trucktrack.dao.entity.TruckEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TruckJpaRepository extends JpaRepository<TruckEntity, Long> {
}
