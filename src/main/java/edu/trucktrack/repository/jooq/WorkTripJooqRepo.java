package edu.trucktrack.repository.jooq;

import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.Pageable;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.jooq.tables.Employee;
import edu.trucktrack.jooq.tables.WorkTrip;
import edu.trucktrack.util.Names;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WorkTripJooqRepo {

    private final DSLContext ctx;

    public List<WorkTripRecordEntity> getWorkTrips(SearchCriteriaRequest criteria) {
        var trip = new WorkTrip(Names.TRIP);
        var emp = new Employee(Names.EMP);
        var filter = criteria.getFilterBy();

        //TODO: implement pagination
        var pagination = criteria.getPageable();

        return ctx.select(
                        trip.ID.as(Names.ID),
                        trip.NAME.as(Names.NAME),
                        trip.DESCRIPTION.as(Names.DESCRIPTION),
                        emp.ID.as(Names.EMPLOYEE_ID),
                        emp.NAME.as(Names.EMPLOYEE_NAME),
                        trip.SALARY.as(Names.SALARY),
                        trip.CURRENCY_ID.as(Names.CURRENCY_ID),
                        trip.CLOSED_AT.as(Names.CLOSED_AT),
                        trip.CREATED_AT.as(Names.CREATED_AT)
                )
                .from(trip)
                .join(emp).on(emp.ID.eq(trip.EMPLOYEE_ID.cast(Long.class)))
                .where(
                        trip.EMPLOYEE_ID.eq(filter.getEmployeeId()).and(emp.COMPANY_ID.eq(filter.getCompanyId()))
                )
                .fetchInto(WorkTripRecordEntity.class);
    }
}
