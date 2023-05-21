package edu.trucktrack.dao.service;


import edu.trucktrack.api.dto.EmployeeDTO;
import edu.trucktrack.api.dto.EmployeeExpensesDTO;
import edu.trucktrack.api.dto.SalaryDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.entity.EmployeeEntity;
import edu.trucktrack.dao.repository.jpa.EmployeeJpaRepository;
import edu.trucktrack.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final SalaryService salaryService;

    private final ExpensesService expensesService;

    private final EmployeeJpaRepository employeeJpaRepository;

    private final EmployeeMapper employeeMapper;

    @Transactional
    public EmployeeEntity save(EmployeeEntity employee) {
        if (employeeJpaRepository.existsByEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Employee with such email already exist");
        }
        return employeeJpaRepository.save(employee);
    }

    public List<EmployeeDTO> getAllForGrid(Long companyId, String currency) {
        SearchCriteriaRequest criteria = SearchCriteriaRequest.builder()
                .filterBy(FilterBy.builder().build())
                .build();

        List<EmployeeDTO> employees = new ArrayList<>();

        for (EmployeeEntity employee : employeeJpaRepository.getByCompanyId(companyId)) {
            criteria.getFilterBy().setEmployeeId(employee.getId().intValue());
            var salaries = salaryService.getAll(criteria, currency);
            var expenses = expensesService.getConvertedTo(criteria, currency);
            EmployeeDTO employeeDTO = employeeMapper.toDTO(employee).toBuilder()
                    .totalSalary(salaries.stream().map(SalaryDTO::getSalary).mapToLong(Double::longValue).sum())
                    .totalExpenses(expenses.stream().mapToLong(EmployeeExpensesDTO::getValue).sum())
                    .totalCurrency(currency)
                    .build();
            employees.add(employeeDTO);
        }

        return employees;
    }

    public EmployeeEntity getByEmail(String email) {
        return employeeJpaRepository.getByEmail(email);
    }

    public EmployeeEntity getById(Long id) {
        return employeeJpaRepository.findById(id).orElse(null);
    }

}
