package edu.trucktrack.repository.jpa;

import edu.trucktrack.entity.TruckEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TruckJpaRepository extends JpaRepository<TruckEntity, Long> {
}
