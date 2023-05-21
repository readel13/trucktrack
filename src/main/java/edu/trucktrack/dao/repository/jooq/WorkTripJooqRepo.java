package edu.trucktrack.dao.repository.jooq;

import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.jooq.tables.Company;
import edu.trucktrack.dao.jooq.tables.Currency;
import edu.trucktrack.dao.jooq.tables.Employee;
import edu.trucktrack.dao.jooq.tables.EmployeeRoles;
import edu.trucktrack.dao.jooq.tables.Roles;
import edu.trucktrack.dao.jooq.tables.SalaryType;
import edu.trucktrack.dao.jooq.tables.Truck;
import edu.trucktrack.dao.jooq.tables.WorkTrip;
import edu.trucktrack.dao.jooq.tables.WorkTripSalaryHistory;
import edu.trucktrack.util.JooqHelper;
import edu.trucktrack.util.Names;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.sum;

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
        var wsh = new WorkTripSalaryHistory(Names.WSH);

        //TODO: implement pagination
        var filter = criteria.getFilterBy();
        var pagination = criteria.getPageable();

        var baseQuery = ctx.selectDistinct(
                        trip.ID.as(Names.ID),
                        trip.NAME.as(Names.NAME),
                        trip.DESCRIPTION.as(Names.DESCRIPTION),
                        emp.ID.as(Names.EMPLOYEE_ID),
                        emp.NAME.as(Names.EMPLOYEE_NAME),
                        truck.ID.as(Names.TRUCK_ID),
                        truck.NAME.as(Names.TRUCK_NAME),
                        JooqHelper.trunc(coalesce(
                                trip.SALARY,
                                sum(trip.SALARY_RATE.mul(wsh.CALCULATION_VALUE)).over(partitionBy(trip.ID)), 0).cast(Double.class),
                                2
                        ).as(Names.SALARY),
                        slt.NAME.as(Names.SALARY_TYPE),
                        trip.SALARY_RATE.as(Names.SALARY_RATE),
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
                .leftJoin(wsh).on(wsh.TRIP_ID.eq(trip.ID.cast(Integer.class)))
                .getQuery();

        Optional.ofNullable(filter.getTripId())
                .ifPresent(tripId -> baseQuery.addConditions(trip.ID.eq(tripId)));

        Optional.ofNullable(filter.getEmployeeId())
                .ifPresent(employeeId -> baseQuery.addConditions(trip.EMPLOYEE_ID.eq(employeeId)));

        Optional.ofNullable(filter.getCompanyId())
                .ifPresent(companyId -> baseQuery.addConditions(emp.COMPANY_ID.eq(companyId)));

        Optional.ofNullable(filter.getName())
                .ifPresent(name -> baseQuery.addConditions(trip.NAME.likeIgnoreCase("%" + name + "%")));

        Optional.ofNullable(filter.getFrom())
                .ifPresent(date -> baseQuery.addConditions(trip.CREATED_AT.ge(date.atStartOfDay())));

        Optional.ofNullable(filter.getTo())
                .ifPresent(date -> baseQuery.addConditions(trip.CREATED_AT.le(date.atStartOfDay())));

        return baseQuery.fetchInto(WorkTripRecordEntity.class);
    }

    public List<DriverDataForMapEntity> getDriverDataForMap() {
        var trip = new WorkTrip(Names.TRIP);
        var emp = new Employee(Names.EMP);
        var truck = new Truck(Names.TRUCK);
        var employeeRoles = new EmployeeRoles(Names.EMPLOYEE_ROLES);
        var roles = new Roles(Names.ROLES);
        var company = new Company(Names.COMPANY);

        var baseQuery  = ctx.select(
                trip.ID.as(Names.ID),
                emp.EMAIL.as(Names.EMAIL),
                emp.NAME.as(Names.NAME),
                truck.NAME.as(Names.NAME),
                truck.TRUCK_NUMBER.as(Names.TRUCK_NUMBER),
                company.ID.as(Names.ID)
        )
                .from(trip)
                .innerJoin(emp).on(emp.ID.eq(trip.EMPLOYEE_ID.cast(Long.class)))
                .innerJoin(truck).on(truck.ID.eq(trip.TRUCK_ID.cast(Long.class)))
                .innerJoin(employeeRoles).on(employeeRoles.EMPLOYEE_ID.eq(emp.ID.cast(Integer.class)))
                .innerJoin(roles).on(roles.ID.eq(employeeRoles.ROLE_ID))
                .innerJoin(company).on(company.ID.eq(emp.COMPANY_ID.cast(Long.class)))
                .where(trip.ACTIVE.eq(true));
        return baseQuery.fetchInto(DriverDataForMapEntity.class);
    }
}
