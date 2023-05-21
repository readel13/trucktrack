package edu.trucktrack.dao.service;

import edu.trucktrack.api.dto.SalaryDTO;
import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.entity.CurrencyEntity;
import edu.trucktrack.dao.entity.EmployeeExpensesEntity;
import edu.trucktrack.dao.entity.WorkTripEntity;
import edu.trucktrack.dao.entity.WorkTripSalaryHistoryEntity;
import edu.trucktrack.dao.entity.enums.SalaryType;
import edu.trucktrack.dao.repository.jooq.SalaryJooqRepository;
import edu.trucktrack.dao.repository.jpa.CurrencyJpaRepository;
import edu.trucktrack.dao.repository.jpa.ExpensesJpaRepository;
import edu.trucktrack.dao.repository.jpa.SalaryJpaRepository;
import edu.trucktrack.dao.repository.jpa.WorkTripJpaRepository;
import edu.trucktrack.dao.repository.jpa.WorkTripSalaryHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryService {

    private final ExchangeRateService exchangeRateService;

    private final SalaryJpaRepository salaryJpaRepository;

    private final SalaryJooqRepository salaryJooqRepository;

    private final WorkTripJpaRepository workTripJpaRepository;

    private final WorkTripSalaryHistoryRepository workTripSalaryHistoryRepository;

    private final ExpensesJpaRepository expensesJpaRepository;

    private final CurrencyJpaRepository currencyJpaRepository;

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

    public WorkTripSalaryHistoryEntity saveDailyDistance(String email, Integer distance) {
        WorkTripEntity workTrip = workTripJpaRepository.findFirstByEmployeeEmailAndActive(email, true);
        return workTripSalaryHistoryRepository.save(
                WorkTripSalaryHistoryEntity.builder()
                        .tripId(workTrip.getId())
                        .calculationValue(distance)
                        .build());
    }

    public Double calculateSalary(WorkTripDTO workTripDTO) {
        List<EmployeeExpensesEntity> expenses = expensesJpaRepository.findAllByTripId(workTripDTO.getId());
        Map<Long, CurrencyEntity> currencyEntities = currencyJpaRepository.findAll().stream().collect(Collectors.toMap(CurrencyEntity::getId, v -> v));
        Long calculatedExpenses =  expenses.stream()
                .map(item -> exchangeRateService.convertTo(
                        Long.valueOf(item.getValue()),
                        currencyEntities.get(Long.valueOf(item.getCurrencyId())).getName(),
                        workTripDTO.getCurrency()))
                .mapToLong(Long::longValue)
                .sum();

        List<WorkTripSalaryHistoryEntity> salaryHistoryEntities = workTripSalaryHistoryRepository.findAllByTripId(workTripDTO.getId());

        Double salary = salaryHistoryEntities.stream().map(item -> workTripDTO.getSalaryRate() * item.getCalculationValue()).mapToDouble(Double::doubleValue).sum();

        return salary + calculatedExpenses;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.DAYS, initialDelay = 1)
    public void addHourlySalaryPerDayEveryDay() {
        System.out.println("asssssssssssssssssssssssssssssssssssss " + LocalDateTime.now().toString());
        List<WorkTripEntity> trips = workTripJpaRepository.findAllBySalaryTypeAndActiveTrue(SalaryType.HOURLY.id());
        List<WorkTripSalaryHistoryEntity> salaries = trips.stream()
                .map(item -> WorkTripSalaryHistoryEntity.builder()
                        .tripId(item.getId())
                        .calculationValue(24)
                        .build())
                .toList();
        workTripSalaryHistoryRepository.saveAll(salaries);
    }
}
