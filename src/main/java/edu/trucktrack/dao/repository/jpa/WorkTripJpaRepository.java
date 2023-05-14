package edu.trucktrack.dao.repository.jpa;

import edu.trucktrack.dao.entity.WorkTripEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkTripJpaRepository extends JpaRepository<WorkTripEntity, Long> {
}
