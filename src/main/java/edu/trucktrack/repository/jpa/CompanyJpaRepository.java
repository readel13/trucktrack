package edu.trucktrack.repository.jpa;

import edu.trucktrack.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, Long> {

    boolean existsByEmail(String email);
}
