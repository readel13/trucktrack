package edu.trucktrack.dao.service;

import edu.trucktrack.api.dto.EmployeeExpensesDTO;
import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.repository.jooq.WorkTripJooqRepo;
import edu.trucktrack.dao.repository.jooq.WorkTripRecordEntity;
import edu.trucktrack.dao.repository.jpa.WorkTripJpaRepository;
import edu.trucktrack.mapper.WorkTripMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkTripService {

    private final WorkTripMapper mapper;

    private final ExpensesService expensesService;

    private final WorkTripJooqRepo workTripJooqRepo;

    private final ExchangeRateService exchangeRateService;

    private final WorkTripJpaRepository workTripJpaRepository;

    @Transactional
    public WorkTripDTO saveOrUpdate(WorkTripDTO workTripDTO) {
        var saved = workTripJpaRepository.save(mapper.toEntity(workTripDTO));

        return mapper.toDTO(saved);
    }

    public List<WorkTripDTO> getAndConvertExpenses(SearchCriteriaRequest criteriaRequest, String convertTo) {
        var tripExpensesMap = expensesService.get(criteriaRequest).stream().collect(Collectors.groupingBy(EmployeeExpensesDTO::getTrip));

        List<WorkTripDTO> result = new ArrayList<>();
        for (var entry : tripExpensesMap.entrySet()) {
            long expenses = entry.getValue().stream()
                    .mapToLong(expensesDTO -> exchangeRateService.convertTo(expensesDTO.getValue(), expensesDTO.getCurrency(), convertTo))
                    .sum();

            WorkTripDTO trip = getById(entry.getKey().getId());
            trip.setCosts((int) expenses);
            result.add(trip);
        }

        result.sort(Comparator.comparing(WorkTripDTO::getCreatedAt));

        return result;
    }


    public List<WorkTripDTO> getAndConvertSalaries(SearchCriteriaRequest criteriaRequest, String convertTo) {
        return get(criteriaRequest).stream()
                .peek(trip -> trip.setSalary(Math.toIntExact(exchangeRateService.convertTo(Long.valueOf(trip.getSalary()), trip.getCurrency(), convertTo))))
                .sorted(Comparator.comparing(WorkTripDTO::getCreatedAt))
                .toList();
    }

    public WorkTripDTO getById(Long id) {
        return get(SearchCriteriaRequest.builder()
                .filterBy(FilterBy.builder().tripId(id).build())
                .build())
                .stream().findFirst().orElse(null);
    }

    //TODO: implement pagination
    public List<WorkTripDTO> get(SearchCriteriaRequest criteria) {
        List<WorkTripRecordEntity> workTrips = workTripJooqRepo.getWorkTrips(criteria);
        return workTrips.stream().map(mapper::toDTO).toList();
    }

    public void delete(Long id) {
        workTripJpaRepository.deleteById(id);
    }
}
