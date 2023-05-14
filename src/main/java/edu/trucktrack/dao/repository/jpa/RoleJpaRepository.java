package edu.trucktrack.dao.repository.jpa;

import edu.trucktrack.dao.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleEntity, Integer> {

	@Query("SELECT r FROM RoleEntity r WHERE r.role IN :roles")
	List<RoleEntity> findByRolesIn(@Param("roles") List<String> roles);
}
