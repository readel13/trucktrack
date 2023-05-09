package edu.trucktrack.repository.jooq;

import edu.trucktrack.api.request.FilterBy;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.jooq.tables.Currency;
import edu.trucktrack.jooq.tables.Employee;
import edu.trucktrack.jooq.tables.EmployeeExpenses;
import edu.trucktrack.jooq.tables.WorkTrip;
import edu.trucktrack.util.Names;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ExpensesJooqRepository {

    private final DSLContext ctx;

    public List<EmployeeExpensesEntityRecord> get(SearchCriteriaRequest criteria) {
        var exps = new EmployeeExpenses(Names.EXPS);
        var trip = new WorkTrip(Names.TRIP);
        var cur = new Currency(Names.CUR);
        var emp = new Employee(Names.EMP);

        var filter = criteria.getFilterBy();

        var query = ctx.select(
                        exps.ID.as(Names.ID),
                        exps.NAME.as(Names.NAME),
                        exps.DESCRIPTION.as(Names.DESCRIPTION),
                        emp.ID.as(Names.EMPLOYEE_ID),
                        emp.NAME.as(Names.EMPLOYEE_NAME),
                        trip.ID.as(Names.TRIP_ID),
                        trip.NAME.as(Names.TRIP_NAME),
                        exps.VALUE.as(Names.VALUE),
                        cur.NAME.as(Names.CURRENCY),
                        exps.CREATED_AT.as(Names.CREATED_AT)
                )
                .from(exps)
                .join(trip).on(exps.TRIP_ID.eq(trip.ID.cast(Integer.class)))
                .join(cur).on(exps.CURRENCY_ID.eq(cur.ID.cast(Integer.class)))
                .join(emp).on(exps.EMPLOYEE_ID.eq(emp.ID.cast(Integer.class)))
                .getQuery();

        Optional.ofNullable(filter.getTripId())
                .ifPresent(tripId -> query.addConditions(trip.ID.eq(tripId)));
        Optional.ofNullable(filter.getCompanyId())
                .ifPresent(companyId -> query.addConditions(emp.COMPANY_ID.eq(companyId)));
        Optional.ofNullable(filter.getName())
                .ifPresent(name -> query.addConditions(exps.NAME.likeIgnoreCase("%" + name + "%")));

        return query.fetchInto(EmployeeExpensesEntityRecord.class);
    }
}
