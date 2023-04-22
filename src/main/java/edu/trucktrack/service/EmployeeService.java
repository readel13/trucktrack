package edu.trucktrack.service;


import edu.trucktrack.entity.EmployeeEntity;
import edu.trucktrack.repository.jpa.EmployeeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeJpaRepository employeeJpaRepository;

    @Transactional
    public EmployeeEntity save(EmployeeEntity employee) {
        if (employeeJpaRepository.existsByEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Employee with such email already exist");
        }

        return employeeJpaRepository.save(employee);
    }
}
