package edu.trucktrack.service;

import edu.trucktrack.api.dto.DriverDataForMapDTO;
import edu.trucktrack.dao.entity.WorkTripCargoHistory;
import edu.trucktrack.dao.repository.jooq.WorkTripJooqRepo;
import edu.trucktrack.repository.jooq.DriverDataForMapEntity;
import edu.trucktrack.repository.jpa.WorkTripCargoHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapService {

	@Value("${apiKey}")
	private String googleMapApiKey;

	private final WorkTripJooqRepo workTripJooqRepo;
	private final WorkTripCargoHistoryRepository workTripCargoHistoryRepository;

	public String getApiKey() {
		return googleMapApiKey;
	}

	public List<DriverDataForMapDTO> getDriverData() {
		List<DriverDataForMapEntity> driverDataForMapEntities = workTripJooqRepo.getDriverDataForMap();
		List<WorkTripCargoHistory> workTripCargoHistories = workTripCargoHistoryRepository.findByTripIn(driverDataForMapEntities.stream().map(DriverDataForMapEntity::getWorkTripId).collect(
				Collectors.toList()));

		return driverDataForMapEntities.stream()
				.map(item -> DriverDataForMapDTO.builder()
						.driverDataForMap(item)
						.cargoNames(workTripCargoHistories.stream().filter(cargo -> cargo.getTrip().getId().equals(item.getWorkTripId())).map(WorkTripCargoHistory::getCargoName).collect(Collectors.toList()))
						.build())
				.collect(Collectors.toList());
	}
}
