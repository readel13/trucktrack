package edu.trucktrack.service;

import edu.trucktrack.dao.entity.WorkTripEntity;
import edu.trucktrack.dao.entity.WorkTripSalaryHistoryEntity;
import edu.trucktrack.dao.entity.enums.SalaryType;
import edu.trucktrack.dao.repository.jpa.WorkTripJpaRepository;
import edu.trucktrack.dao.repository.jpa.WorkTripSalaryHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryService {

	private final WorkTripJpaRepository workTripJpaRepository;
	private final WorkTripSalaryHistoryRepository workTripSalaryHistoryRepository;

	public WorkTripSalaryHistoryEntity saveDailyDistance(String email, Integer distance) {
		WorkTripEntity workTrip = workTripJpaRepository.findFirstByEmployeeEmailAndActive(email, true);
		return workTripSalaryHistoryRepository.save(
				WorkTripSalaryHistoryEntity.builder()
						.tripId(workTrip.getId())
						.calculationValue(distance)
						.build());
	}

	@Scheduled(fixedDelay = 1, timeUnit = TimeUnit.DAYS)
	public void addHourlySalaryPerDayEveryDay() {
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
