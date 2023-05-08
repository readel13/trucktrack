package edu.trucktrack.repository.jpa;

import edu.trucktrack.entity.EmployeeExpensesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpensesJpaRepository extends JpaRepository<EmployeeExpensesEntity, Long> {
}
