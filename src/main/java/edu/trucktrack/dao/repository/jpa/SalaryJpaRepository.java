package edu.trucktrack.dao.repository.jpa;

import edu.trucktrack.dao.entity.WorkTripSalaryHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryJpaRepository extends JpaRepository<WorkTripSalaryHistoryEntity, Long> {
}
