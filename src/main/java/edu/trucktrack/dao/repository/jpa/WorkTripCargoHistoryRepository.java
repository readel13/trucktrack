package edu.trucktrack.dao.repository.jpa;


import edu.trucktrack.dao.entity.WorkTripCargoHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkTripCargoHistoryRepository extends JpaRepository<WorkTripCargoHistory, Long> {

    @Query("SELECT w FROM WorkTripCargoHistory w WHERE w.trip.id IN :tripIds AND w.deliveredAt = null")
    List<WorkTripCargoHistory> findByTripIn(@Param("tripIds") List<Long> tripIds);
}
