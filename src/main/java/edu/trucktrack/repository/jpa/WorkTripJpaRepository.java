package edu.trucktrack.repository.jpa;

import edu.trucktrack.entity.WorkTripEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkTripJpaRepository extends JpaRepository<WorkTripEntity, Long> {
}
