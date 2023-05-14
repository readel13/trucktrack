package edu.trucktrack.dao.repository.jpa;

import edu.trucktrack.dao.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, Long> {

    boolean existsByEmail(String email);
}
