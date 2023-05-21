package edu.trucktrack.dao.service;

import edu.trucktrack.api.dto.EmployeeExpensesDTO;
import edu.trucktrack.api.dto.TagDTO;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.repository.jooq.EmployeeExpensesEntityRecord;
import edu.trucktrack.dao.repository.jooq.ExpensesJooqRepository;
import edu.trucktrack.dao.repository.jooq.TagJooqRepository;
import edu.trucktrack.dao.repository.jpa.ExpensesJpaRepository;
import edu.trucktrack.mapper.EmployeeExpensesMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpensesService {

    private final EmployeeExpensesMapper mapper;

    private final TagJooqRepository tagJooqRepository;

    private final ExchangeRateService exchangeRateService;

    private final ExpensesJpaRepository expensesJpaRepository;

    private final ExpensesJooqRepository expensesJooqRepository;

    @Transactional
    public void saveOrUpdate(EmployeeExpensesDTO employeeExpensesDTO) {
        var entity = mapper.toEntity(employeeExpensesDTO);

        expensesJpaRepository.save(entity);
    }

    public Map<TagDTO, List<EmployeeExpensesDTO>> getGrouppedByTag(SearchCriteriaRequest criteria, String currency) {
        var expenses = getConvertedTo(criteria, currency);

        return expenses.stream()
                .map(this::normalizeExpense)
                .map(EmployeeExpensesDTO::getTags)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Function.identity(), v -> findByTag(expenses, v), (first, second) -> first));
    }

    public List<EmployeeExpensesDTO> getConvertedTo(SearchCriteriaRequest criteria, String currency) {
        return get(criteria).stream()
                .peek(e -> e.setValue(exchangeRateService.convertTo(e.getValue(), e.getCurrency(), currency)))
                .peek(e -> e.setCurrency(currency))
                .toList();
    }


    public void deleteById(Long id) {
        expensesJpaRepository.deleteById(id);
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

    public EmployeeExpensesDTO normalizeExpense(EmployeeExpensesDTO expensesDTO) {
        Integer numOfTags = Optional.ofNullable(expensesDTO.getTags())
                .map(Set::size)
                .filter(size -> size > 0)
                .orElse(1);
        expensesDTO.setValue(expensesDTO.getValue() / numOfTags);
        return expensesDTO;
    }

    public List<EmployeeExpensesDTO> findByTag(List<EmployeeExpensesDTO> expenses, TagDTO tag) {
        return expenses.stream().filter(e -> e.getTags().contains(tag)).toList();
    }
}
