package edu.trucktrack.dao.service;

import edu.trucktrack.api.dto.SalaryDTO;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.repository.jooq.SalaryJooqRepository;
import edu.trucktrack.dao.repository.jpa.SalaryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryService {

    private final ExchangeRateService exchangeRateService;

    private final SalaryJpaRepository salaryJpaRepository;

    private final SalaryJooqRepository salaryJooqRepository;

    public List<SalaryDTO> getAll(SearchCriteriaRequest criteriaRequest, String currency) {
        List<SalaryDTO> allSalaries = salaryJooqRepository.getAllSalaries(criteriaRequest);

        if (currency == null || !allSalaries.isEmpty() && allSalaries.get(0).getCurrency().equals(currency)) {
            return allSalaries;
        }

        return allSalaries.stream()
                .peek(salary -> salary.setSalary(exchangeRateService.convertTo(salary.getSalary().longValue(), salary.getCurrency(), currency).doubleValue()))
                .peek(salary -> salary.setCurrency(currency))
                .toList();
    }

    public void deleteById(Long id) {
        salaryJpaRepository.deleteById(id);
    }
}
