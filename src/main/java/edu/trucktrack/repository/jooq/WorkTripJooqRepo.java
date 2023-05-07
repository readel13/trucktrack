package edu.trucktrack.repository.jooq;

import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.jooq.tables.Currency;
import edu.trucktrack.jooq.tables.Employee;
import edu.trucktrack.jooq.tables.SalaryType;
import edu.trucktrack.jooq.tables.Truck;
import edu.trucktrack.jooq.tables.WorkTrip;
import edu.trucktrack.util.Names;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WorkTripJooqRepo {

    private final DSLContext ctx;

    public List<WorkTripRecordEntity> getWorkTrips(SearchCriteriaRequest criteria) {
        var trip = new WorkTrip(Names.TRIP);
        var emp = new Employee(Names.EMP);
        var truck = new Truck(Names.TRUCK);
        var slt = new SalaryType(Names.SLT);
        var cur = new Currency(Names.CUR);

        //TODO: implement pagination
        var filter = criteria.getFilterBy();
        var pagination = criteria.getPageable();

        var baseQuery = ctx.select(
                        trip.ID.as(Names.ID),
                        trip.NAME.as(Names.NAME),
                        trip.DESCRIPTION.as(Names.DESCRIPTION),
                        emp.ID.as(Names.EMPLOYEE_ID),
                        emp.NAME.as(Names.EMPLOYEE_NAME),
                        truck.ID.as(Names.TRUCK_ID),
                        truck.NAME.as(Names.TRUCK_NAME),
                        trip.SALARY.as(Names.SALARY),
                        slt.NAME.as(Names.SALARY_TYPE),
                        cur.NAME.as(Names.CURRENCY),
                        trip.ACTIVE.as(Names.ACTIVE),
                        trip.CLOSED_AT.as(Names.CLOSED_AT),
                        trip.CREATED_AT.as(Names.CREATED_AT)
                )
                .from(trip)
                .join(emp).on(emp.ID.eq(trip.EMPLOYEE_ID.cast(Long.class)))
                .join(truck).on(truck.ID.eq(trip.TRUCK_ID.cast(Long.class)))
                .join(slt).on(slt.ID.eq(trip.SALARY_TYPE.cast(Long.class)))
                .join(cur).on(cur.ID.eq(trip.CURRENCY_ID.cast(Long.class)))
                .getQuery();

        Optional.ofNullable(filter.getTripId())
                .ifPresent(tripId -> baseQuery.addConditions(trip.ID.eq(tripId)));

        Optional.ofNullable(filter.getEmployeeId())
                        .ifPresent(employeeId -> baseQuery.addConditions(trip.EMPLOYEE_ID.eq(employeeId)));

        Optional.ofNullable(filter.getCompanyId())
                .ifPresent(companyId -> baseQuery.addConditions(emp.COMPANY_ID.eq(companyId)));

        Optional.ofNullable(filter.getName())
                .ifPresent(name -> baseQuery.addConditions(trip.NAME.likeIgnoreCase("%" + name + "%")));

        return baseQuery.fetchInto(WorkTripRecordEntity.class);
    }
}
