package edu.trucktrack.dao.service;

import edu.trucktrack.dao.entity.CompanyEntity;
import edu.trucktrack.dao.entity.EmployeeEntity;
import edu.trucktrack.dao.repository.jpa.CompanyJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyJpaRepository companyJpaRepository;

    private final EmployeeService employeeService;

    @Transactional
    public CompanyEntity save(CompanyEntity company, EmployeeEntity employee) {
        if (companyJpaRepository.existsByEmail(company.getEmail())) {
            throw new IllegalArgumentException("Company with such email already exist");
        }

        var savedCompany = companyJpaRepository.save(company);

        //TODO: make it owner(boolean field in db or add security role)
        employee = employee.toBuilder()
                .company(company)
                .active(true)
                .build();

        employeeService.save(employee);

        return savedCompany;
    }

    @Transactional(readOnly = true)
    public CompanyEntity getById(Long id) {
        return companyJpaRepository.findById(id).orElse(null);
    }

    @Transactional
    public CompanyEntity update(CompanyEntity company) {
        if (companyJpaRepository.findById(company.getId()).isEmpty()) {
            throw new IllegalArgumentException("Entity not found");
        }
        return companyJpaRepository.save(company);
    }
}
