package edu.trucktrack.repository.jooq;

import edu.trucktrack.api.dto.TruckDTO;
import edu.trucktrack.jooq.tables.Truck;
import edu.trucktrack.util.Names;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TruckJooqRepository {

    private final DSLContext ctx;

    public Boolean validateForSave(TruckDTO dto) {
        Truck truck = new Truck(Names.TRUCK);
        return ctx.select(DSL.countDistinct(truck.ID).gt(0))
                .from(truck)
                .where(truck.TRUCK_NUMBER.eq(dto.getTruckNumber())
                        .or(truck.VIN_CODE.eq(dto.getVinCode()))
                )
                .fetchOneInto(Boolean.class);
    }
}
