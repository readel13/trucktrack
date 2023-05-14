package edu.trucktrack.mapper;

import edu.trucktrack.api.dto.EmployeeExpensesDTO;
import edu.trucktrack.dao.entity.EmployeeExpensesEntity;
import edu.trucktrack.dao.entity.enums.Currency;
import edu.trucktrack.dao.repository.jooq.EmployeeExpensesEntityRecord;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {WorkTripMapper.class}
)
public abstract class EmployeeExpensesMapper {

    @Autowired
    protected WorkTripMapper workTripMapper;

    @Mapping(target = "trip", expression = "java(workTripMapper.toSimpleEntity(dto.getTrip()))")
    @Mapping(source = "currency", target = "currencyId", qualifiedByName = "mapCurrencyLabel")
    public abstract EmployeeExpensesEntity toEntity(EmployeeExpensesDTO dto);

    @Mapping(source = "tripId", target = "trip.id")
    @Mapping(source = "tripName", target = "trip.name")
    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "employeeName", target = "employee.name")
    public abstract EmployeeExpensesDTO toDTO(EmployeeExpensesEntityRecord entityRecord);

    @Named("mapCurrencyId")
    static String mapCurrencyId(Integer currencyId) {
        return Currency.findById(currencyId).label();
    }

    @Named("mapCurrencyLabel")
    static Integer mapCurrencyLabel(String label) {
        return Currency.findByLabel(label).id();
    }
}
