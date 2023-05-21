package edu.trucktrack.dao.service;

import edu.trucktrack.api.dto.CargoDTO;
import edu.trucktrack.api.dto.EmployeeDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.entity.EmployeeEntity;
import edu.trucktrack.dao.entity.WorkTripCargoHistory;
import edu.trucktrack.dao.entity.WorkTripEntity;
import edu.trucktrack.dao.repository.jooq.EmployeeEntityRecord;
import edu.trucktrack.dao.repository.jooq.WorkTripCargoHistoryJooqRepo;
import edu.trucktrack.dao.repository.jpa.EmployeeJpaRepository;
import edu.trucktrack.dao.repository.jpa.WorkTripJpaRepository;
import edu.trucktrack.repository.jpa.WorkTripCargoHistoryRepository;
import edu.trucktrack.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CargoService {

	private final String NEW_CARGO_MSG = "You have new cargo: \n Cargo number: %s \n Cargo name: %s \n Weight: %s \n Loading address: %s \n Loading time: %s \n Unloading address: %s \n Unloading time: %s";
	private final String UPDATE_CARGO_MSG = "Your cargo was update: \n Cargo number: %s \n Cargo name: %s \n Weight: %s \n Loading address: %s \n Loading time: %s \n Unloading address: %s \n Unloading time: %s";

	private final String DELETE_CARGO = "Yours cargo was deleted: \n Cargo number: %s";

	private final WorkTripCargoHistoryJooqRepo workTripCargoHistoryJooqRepo;
	private final WorkTripCargoHistoryRepository workTripCargoHistoryRepository;
	private final WorkTripJpaRepository workTripJpaRepository;
	private final EmployeeJpaRepository employeeJpaRepository;
	private final EmailUtil emailUtil;

	public List<CargoDTO> findAllCargosForCompanyByEmail(SearchCriteriaRequest request) {
		EmployeeEntity employee = employeeJpaRepository.getByEmail(request.getFilterBy().getEmail());
		request.getFilterBy().setCompanyId(employee.getCompany().getId().intValue());
		return workTripCargoHistoryJooqRepo.getCargosByEmployeeCompany(request);
	}

	public List<EmployeeEntityRecord> getEmployeeForCreatingCargo(SearchCriteriaRequest request) {
		EmployeeEntity employee = employeeJpaRepository.getByEmail(request.getFilterBy().getEmail());
		request.getFilterBy().setCompanyId(employee.getCompany().getId().intValue());
		return workTripCargoHistoryJooqRepo.getAllWorkingDriversByCompanyId(request);
	}

	public void saveCargo(CargoDTO cargoDTO, String email) {
		WorkTripEntity workTrip = workTripJpaRepository.findById(cargoDTO.getTripId()).get();
		WorkTripCargoHistory cargoHistory = WorkTripCargoHistory.builder()
				.trip(workTrip)
				.cargoName(cargoDTO.getCargoName())
				.cargoDescription(cargoDTO.getCargoDescription())
				.cargoWeight(cargoDTO.getCargoWeight())
				.loadingLocation(cargoDTO.getLoadingLocation())
				.loadingTime(cargoDTO.getLoadingTime())
				.unloadingLocation(cargoDTO.getUnloadingLocation())
				.unloadingTime(cargoDTO.getUnloadingTime())
				.createdAt(cargoDTO.getCreatedAt())
				.build();
		WorkTripCargoHistory result =  workTripCargoHistoryRepository.save(cargoHistory);
		if (result != null) {
			emailUtil.sendMessage(email, String.format(NEW_CARGO_MSG, result.getId(), cargoDTO.getCargoName(), cargoDTO.getCargoWeight(), cargoDTO.getLoadingLocation(), cargoDTO.getLoadingTime().toString(), cargoDTO.getUnloadingLocation(), cargoDTO.getUnloadingTime().toString()));
		}
	}

	public void updateCargo(CargoDTO cargoDTO, String email, String oldEmail) {
		WorkTripEntity workTrip = workTripJpaRepository.findById(cargoDTO.getTripId()).get();
		WorkTripCargoHistory cargoHistory = WorkTripCargoHistory.builder()
				.id(cargoDTO.getId())
				.trip(workTrip)
				.cargoName(cargoDTO.getCargoName())
				.cargoDescription(cargoDTO.getCargoDescription())
				.cargoWeight(cargoDTO.getCargoWeight())
				.loadingLocation(cargoDTO.getLoadingLocation())
				.loadingTime(cargoDTO.getLoadingTime())
				.unloadingLocation(cargoDTO.getUnloadingLocation())
				.unloadingTime(cargoDTO.getUnloadingTime())
				.createdAt(cargoDTO.getCreatedAt())
				.build();
		WorkTripCargoHistory result =  workTripCargoHistoryRepository.save(cargoHistory);
		if (result != null) {
			if (email.equals(oldEmail)) {
				emailUtil.sendMessage(email, String.format(UPDATE_CARGO_MSG, result.getId(),cargoDTO.getCargoName(), cargoDTO.getCargoWeight(), cargoDTO.getLoadingLocation(), cargoDTO.getLoadingTime(), cargoDTO.getUnloadingLocation(), cargoDTO.getUnloadingTime()));
			} else {
				emailUtil.sendMessage(oldEmail, String.format(DELETE_CARGO, result.getId()));
				emailUtil.sendMessage(email, String.format(NEW_CARGO_MSG, result.getId(),cargoDTO.getCargoName(), cargoDTO.getCargoWeight(), cargoDTO.getLoadingLocation(), cargoDTO.getLoadingTime(), cargoDTO.getUnloadingLocation(), cargoDTO.getUnloadingTime()));
			}
		}
	}

	public void deleteCargo(CargoDTO cargoDTO) {
		workTripCargoHistoryRepository.deleteById(cargoDTO.getId());
		emailUtil.sendMessage(cargoDTO.getDriverEmail(), String.format(DELETE_CARGO, cargoDTO.getId()));
	}

	public void closeCargo(CargoDTO dto) {
		WorkTripCargoHistory cargoHistory = workTripCargoHistoryRepository.findById(dto.getId()).get();
		cargoHistory.setDeliveredAt(LocalDateTime.now());
		workTripCargoHistoryRepository.save(cargoHistory);
	}
}
