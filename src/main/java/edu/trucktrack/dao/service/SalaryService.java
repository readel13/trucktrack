package edu.trucktrack.dao.service;

import edu.trucktrack.api.dto.SalaryDTO;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.repository.jooq.SalaryJooqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryService {

    private final SalaryJooqRepository salaryJooqRepository;

    public List<SalaryDTO> getAll(SearchCriteriaRequest criteriaRequest) {
        return salaryJooqRepository.getAllSalaries(criteriaRequest);
    }
}
