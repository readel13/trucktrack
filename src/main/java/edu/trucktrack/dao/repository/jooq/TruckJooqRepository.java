package edu.trucktrack.dao.repository.jooq;

import edu.trucktrack.api.dto.TruckDTO;
import edu.trucktrack.dao.jooq.tables.Company;
import edu.trucktrack.dao.jooq.tables.Employee;
import edu.trucktrack.dao.jooq.tables.Truck;
import edu.trucktrack.dao.jooq.tables.WorkTrip;
import edu.trucktrack.util.Names;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.select;

@Repository
@RequiredArgsConstructor
public class TruckJooqRepository {

    private final DSLContext ctx;

    public Boolean validateForSave(TruckDTO dto) {
        var truck = new Truck(Names.TRUCK);
        return ctx.select(DSL.countDistinct(truck.ID).gt(0))
                .from(truck)
                .where(truck.TRUCK_NUMBER.eq(dto.getTruckNumber())
                        .or(truck.VIN_CODE.eq(dto.getVinCode()))
                )
                .fetchOneInto(Boolean.class);
    }

    public List<TruckDTO> getAllAvailableForEmployee(Long employeeId) {
        var truck = new Truck(Names.TRUCK);
        var subTruck = new Truck(Names.SUB_TRUCK);
        var workTrip = new WorkTrip(Names.WORK_TRIP);
        var cmp = new Company(Names.CMP);
        var emp = new Employee(Names.EMP);

        return ctx.select(
                        truck.ID.as(Names.ID),
                        truck.NAME.as(Names.NAME),
                        truck.COMPANY_ID.as(Names.COMPANY_ID),
                        truck.TRUCK_NUMBER.as(Names.TRUCK_NUMBER),
                        truck.VIN_CODE.as(Names.VIN_CODE),
                        truck.FUEL_CONSUMPTION.as(Names.FUEL_CONSUMPTION),
                        truck.ACTIVE.as(Names.ACTIVE),
                        truck.CREATED_AT.as(Names.CREATED_AT)
                )
                .from(truck)
                .join(cmp).on(truck.COMPANY_ID.eq(cmp.ID.cast(Integer.class)))
                .join(emp).on(cmp.ID.eq(emp.COMPANY_ID.cast(Long.class)))
                .where(emp.ID.eq(employeeId)
                        .and(truck.ACTIVE.isTrue()).and(notExists(
                        select(asterisk())
                                .from(subTruck)
                                .join(workTrip).on(workTrip.TRUCK_ID.eq(subTruck.ID.cast(Integer.class))
                                        .and(workTrip.EMPLOYEE_ID.eq(emp.ID.cast(Integer.class))))
                                .where(subTruck.ID.eq(truck.ID).and(
                                        workTrip.ACTIVE.isTrue().or(workTrip.CLOSED_AT.isNull())
                                ))
                )))
                .fetchInto(TruckDTO.class);
    }
}
