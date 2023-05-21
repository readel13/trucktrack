package edu.trucktrack.dao.repository.jooq;

import edu.trucktrack.api.dto.SalaryDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.jooq.tables.Currency;
import edu.trucktrack.dao.jooq.tables.Employee;
import edu.trucktrack.dao.jooq.tables.WorkTrip;
import edu.trucktrack.dao.jooq.tables.WorkTripSalaryHistory;
import edu.trucktrack.util.Names;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SalaryJooqRepository {

    private final DSLContext ctx;

    public List<SalaryDTO> getAllSalaries(SearchCriteriaRequest criteriaRequest) {
        var cur = new Currency(Names.CUR);
        var workTrip = new WorkTrip(Names.WORK_TRIP);
        var wsh = new WorkTripSalaryHistory(Names.WSH);
        var emp = new Employee(Names.EMP);

        var filter = criteriaRequest.getFilterBy();

        var baseQuery = ctx.select(
                        wsh.ID.as(Names.ID),
                        workTrip.ID.as(Names.TRIP_ID),
                        workTrip.NAME.as(Names.TRIP_NAME),
                        wsh.CALCULATION_VALUE.as(Names.CALCULATION_VALUE),
                        wsh.CALCULATION_VALUE.mul(workTrip.SALARY_TYPE).as(Names.SALARY),
                        cur.NAME.as(Names.CURRENCY),
                        wsh.CREATED_AT.as(Names.CREATED_AT)
                )
                .from(wsh)
                .join(workTrip).on(wsh.TRIP_ID.eq(workTrip.ID.cast(Integer.class)))
                .join(emp).on(workTrip.EMPLOYEE_ID.eq(emp.ID.cast(Integer.class)))
                .join(cur).on(workTrip.CURRENCY_ID.eq(cur.ID.cast(Integer.class)))
                .getQuery();

        Optional.ofNullable(filter.getTripId())
                .ifPresent(tripId -> baseQuery.addConditions(workTrip.ID.eq(tripId)));

        Optional.ofNullable(filter.getEmployeeId())
                .map(Integer::longValue)
                .ifPresent(employeeId -> baseQuery.addConditions(emp.ID.eq(employeeId)));

        return baseQuery.fetchInto(SalaryDTO.class);
    }
}
