package edu.trucktrack.dao.repository.jooq;

import edu.trucktrack.api.dto.TruckDTO;
import edu.trucktrack.api.request.SearchCriteriaRequest;
import edu.trucktrack.dao.jooq.tables.Company;
import edu.trucktrack.dao.jooq.tables.Employee;
import edu.trucktrack.dao.jooq.tables.Truck;
import edu.trucktrack.dao.jooq.tables.WorkTrip;
import edu.trucktrack.util.Names;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.SelectConditionStep;
import org.jooq.SelectFieldOrAsterisk;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.countDistinct;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.select;

@Repository
@RequiredArgsConstructor
public class TruckJooqRepository {

    private final DSLContext ctx;

    public Boolean exist(TruckDTO dto) {
        var truck = new Truck(Names.TRUCK);
        return ctx.select(countDistinct(truck.ID).gt(0))
                .from(truck)
                .where(truck.TRUCK_NUMBER.eq(dto.getTruckNumber()).or(truck.VIN_CODE.eq(dto.getVinCode())))
                .fetchOneInto(Boolean.class);
    }

    public List<TruckDTO> getAll(SearchCriteriaRequest criteriaRequest) {
        var truck = new Truck(Names.TRUCK);
        var subTruck = new Truck(Names.SUB_TRUCK);
        var workTrip = new WorkTrip(Names.WORK_TRIP);
        var cmp = new Company(Names.CMP);

        var existCondition = countDistinct(subTruck.ID).eq(0);

        var filter = criteriaRequest.getFilterBy();

        var query = ctx.select(
                        truck.ID.as(Names.ID),
                        truck.NAME.as(Names.NAME),
                        truck.COMPANY_ID.as(Names.COMPANY_ID),
                        cmp.NAME.as(Names.COMPANY_NAME),
                        truck.TRUCK_NUMBER.as(Names.TRUCK_NUMBER),
                        truck.VIN_CODE.as(Names.VIN_CODE),
                        truck.FUEL_CONSUMPTION.as(Names.FUEL_CONSUMPTION),
                        truck.ACTIVE.as(Names.ACTIVE),
                        availableTruckQuery(existCondition, truck, cmp, workTrip).asField(Names.AVAILABLE.first()),
                        truck.CREATED_AT.as(Names.CREATED_AT)
                )
                .from(truck)
                .join(cmp).on(truck.COMPANY_ID.eq(cmp.ID.cast(Integer.class)))
                .getQuery();

        Optional.ofNullable(filter.getCompanyId())
                .map(Long::valueOf)
                .ifPresent(id -> query.addConditions(cmp.ID.eq(id)));

        return query.fetchInto(TruckDTO.class);
    }

    public List<TruckDTO> getAllAvailableForEmployee(Long employeeId) {
        var truck = new Truck(Names.TRUCK);
        var workTrip = new WorkTrip(Names.WORK_TRIP);
        var cmp = new Company(Names.CMP);
        var emp = new Employee(Names.EMP);

        return ctx.select(
                        truck.ID.as(Names.ID),
                        truck.NAME.as(Names.NAME),
                        truck.COMPANY_ID.as(Names.COMPANY_ID),
                        cmp.NAME.as(Names.COMPANY_NAME),
                        truck.TRUCK_NUMBER.as(Names.TRUCK_NUMBER),
                        truck.VIN_CODE.as(Names.VIN_CODE),
                        truck.FUEL_CONSUMPTION.as(Names.FUEL_CONSUMPTION),
                        truck.ACTIVE.as(Names.ACTIVE),
                        inline(Boolean.TRUE).as(Names.AVAILABLE),
                        truck.CREATED_AT.as(Names.CREATED_AT)
                )
                .from(truck)
                .join(cmp).on(truck.COMPANY_ID.eq(cmp.ID.cast(Integer.class)))
                .join(emp).on(cmp.ID.eq(emp.COMPANY_ID.cast(Long.class)))
                .where(emp.ID.eq(employeeId)
                        .and(truck.ACTIVE.isTrue()).and(notExists(availableTruckQuery(asterisk(), truck, cmp, workTrip))))
                .fetchInto(TruckDTO.class);
    }

    private SelectConditionStep<?> availableTruckQuery(SelectFieldOrAsterisk selectField, Truck truck, Company cmp, WorkTrip workTrip) {
        var subEmp = new Employee(Names.SUB_EMP);
        var subTruck = new Truck(Names.SUB_TRUCK);

        return select(selectField)
                .from(subTruck)
                .join(subEmp).on(subEmp.COMPANY_ID.eq(cmp.ID.cast(Integer.class)))
                .join(workTrip).on(workTrip.TRUCK_ID.eq(subTruck.ID.cast(Integer.class)).and(workTrip.EMPLOYEE_ID.eq(subEmp.ID.cast(Integer.class))))
                .where(subTruck.ID.eq(truck.ID).and(workTrip.ACTIVE.isTrue().or(workTrip.CLOSED_AT.isNull())));
    }
}
