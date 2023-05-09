package edu.trucktrack.service;

import edu.trucktrack.api.dto.EmployeeExpensesDTO;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.entity.EmployeeExpensesEntity;
import edu.trucktrack.jooq.tables.EmployeeExpenses;
import edu.trucktrack.mapper.EmployeeExpensesMapper;
import edu.trucktrack.repository.jooq.EmployeeExpensesEntityRecord;
import edu.trucktrack.repository.jooq.ExpensesJooqRepository;
import edu.trucktrack.repository.jooq.TagJooqRepository;
import edu.trucktrack.repository.jpa.ExpensesJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpensesService {

    private final EmployeeExpensesMapper mapper;

    private final TagJooqRepository tagJooqRepository;

    private final ExpensesJpaRepository expensesJpaRepository;

    private final ExpensesJooqRepository expensesJooqRepository;

    @Transactional
    public EmployeeExpensesEntity saveOrUpdate(EmployeeExpensesDTO employeeExpensesDTO) {
        var entity = mapper.toEntity(employeeExpensesDTO);

        return expensesJpaRepository.save(entity);
    }

    public List<EmployeeExpensesDTO> get(SearchCriteriaRequest criteria) {
        var expenses = expensesJooqRepository.get(criteria);
        var ids = expenses.stream().map(EmployeeExpensesEntityRecord::getId).collect(Collectors.toSet());

        var expnseTagMap = tagJooqRepository.getForExpenses(ids);

         return expenses.stream()
                 .map(mapper::toDTO)
                 .peek(ex -> ex.setTags(Optional.ofNullable(expnseTagMap.get(ex.getId().intValue())).orElse(Collections.emptySet())))
                 .toList();

    }
}
