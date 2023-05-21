package edu.trucktrack.dao.repository.jooq;

import edu.trucktrack.api.dto.CargoDTO;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.jooq.tables.Employee;
import edu.trucktrack.dao.jooq.tables.WorkTrip;
import edu.trucktrack.dao.jooq.tables.WorkTripCargoHistory;
import edu.trucktrack.util.Names;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WorkTripCargoHistoryJooqRepo {

	private final DSLContext ctx;

	public List<CargoDTO> getCargosByEmployeeCompany(SearchCriteriaRequest request) {
		WorkTripCargoHistory cargoHistory = new WorkTripCargoHistory(Names.CARGO);
		WorkTrip workTrip = new WorkTrip(Names.WORK_TRIP);
		Employee employee = new Employee(Names.EMPLOYEE);

		var filter = request.getFilterBy();

		var query = ctx.selectDistinct(
				cargoHistory.ID.as(Names.ID),
				cargoHistory.TRIP_ID.as(Names.TRIP_ID),
				workTrip.EMPLOYEE_ID.as(Names.EMPLOYEE_ID),
				employee.NAME.as(Names.NAME),
				employee.EMAIL.as(Names.EMAIL),
				cargoHistory.CARGO_NAME.as(Names.CARGO_NAME),
				cargoHistory.CARGO_DESCRIPTION.as(Names.CARGO_DESCRIPTION),
				cargoHistory.CARGO_WEIGHT.as(Names.CARGO_WEIGHT),
				cargoHistory.DISTANCE.as(Names.DISTANCE),
				cargoHistory.LOADING_LOCATION.as(Names.LOADING_LOCATION),
				cargoHistory.LOADING_TIME.as(Names.LOADING_TIME),
				cargoHistory.UNLOADING_LOCATION.as(Names.UNLOADING_LOCATION),
				cargoHistory.UNLOADING_TIME.as(Names.UNLOADING_TIME),
				cargoHistory.CREATED_AT.as(Names.CREATED_AT),
				cargoHistory.DELIVERED_AT.as(Names.DELIVERED_AT)
		)
				.from(cargoHistory)
				.innerJoin(workTrip).on(cargoHistory.TRIP_ID.eq(workTrip.ID.cast(Integer.class)))
				.innerJoin(employee).on(workTrip.EMPLOYEE_ID.eq(employee.ID.cast(Integer.class)))
				.where(cargoHistory.CREATED_AT.between(LocalDateTime.now().minusMonths(1), LocalDateTime.now()))
				.getQuery();

		Optional.ofNullable(filter.getCompanyId())
				.ifPresent(companyId -> query.addConditions(employee.COMPANY_ID.eq(companyId)));

		Optional.ofNullable(filter.getName())
				.ifPresent(name -> query.addConditions(cargoHistory.CARGO_NAME.eq(name)));

		Optional.ofNullable(filter.getEmployeeName())
				.ifPresent(employeeName -> query.addConditions(employee.NAME.eq(employeeName)));


		Optional.ofNullable(filter.getLoadingLocation())
				.ifPresent(loadLoc -> query.addConditions(cargoHistory.LOADING_LOCATION.eq(loadLoc)));

		Optional.ofNullable(filter.getUnloadingLocation())
				.ifPresent(unloadLoc -> query.addConditions(cargoHistory.UNLOADING_LOCATION.eq(unloadLoc)));

		return query.fetchInto(CargoDTO.class);
	}

	public List<EmployeeEntityRecord> getAllWorkingDriversByCompanyId(SearchCriteriaRequest request) {
		WorkTrip workTrip = new WorkTrip(Names.WORK_TRIP);
		Employee employee = new Employee(Names.EMPLOYEE);

		var filter = request.getFilterBy();

		var query = ctx.selectDistinct(
				employee.ID.as(Names.ID),
				employee.NAME.as(Names.NAME),
				employee.EMAIL.as(Names.EMAIL),
				workTrip.ID.as(Names.ID)
		).from(employee)
				.innerJoin(workTrip).on(workTrip.EMPLOYEE_ID.eq(employee.ID.cast(Integer.class)))
				.where(workTrip.CLOSED_AT.isNull())
				.getQuery();

		Optional.ofNullable(filter.getCompanyId())
				.ifPresent(id -> query.addConditions(employee.COMPANY_ID.eq(id)));

		return query.fetchInto(EmployeeEntityRecord.class);
	}

}
