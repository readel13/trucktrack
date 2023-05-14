package edu.trucktrack.dao.repository.jpa;

import edu.trucktrack.dao.entity.EmployeeExpensesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpensesJpaRepository extends JpaRepository<EmployeeExpensesEntity, Long> {
}
