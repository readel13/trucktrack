package edu.trucktrack.dao.repository.jpa;

import edu.trucktrack.dao.entity.WorkTripSalaryHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkTripSalaryHistoryRepository extends JpaRepository<WorkTripSalaryHistoryEntity, Long> {

	List<WorkTripSalaryHistoryEntity> findAllByTripId(Long tripId);
}
