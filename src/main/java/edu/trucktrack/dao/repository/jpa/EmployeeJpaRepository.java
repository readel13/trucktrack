package edu.trucktrack.dao.repository.jpa;

import edu.trucktrack.dao.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, Long> {

    boolean existsByEmail(String email);
    EmployeeEntity getByEmail(String email);
}
