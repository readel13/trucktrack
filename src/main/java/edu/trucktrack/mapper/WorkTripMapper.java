package edu.trucktrack.mapper;

import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.dao.entity.WorkTripEntity;
import edu.trucktrack.dao.entity.enums.Currency;
import edu.trucktrack.dao.entity.enums.SalaryType;
import edu.trucktrack.dao.repository.jooq.WorkTripRecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface WorkTripMapper {

    @Mapping(source = "truck.id", target = "truckId")
    @Mapping(source = "truck.name", target = "truckName")
    @Mapping(source = "currencyId", target = "currency", qualifiedByName = "mapCurrencyIdToCurrencyLabel")
    @Mapping(source = "salaryType", target = "salaryType", qualifiedByName = "mapSalaryTypeToEnumName")
    WorkTripDTO toDTO(WorkTripEntity entity);

    @Mapping(source = "salaryType", target = "salaryType", ignore = true)
    WorkTripEntity toSimpleEntity(WorkTripDTO dto);

    @Mapping(source = "truckId", target = "truck.id")
    @Mapping(source = "truckName", target = "truck.name")
    @Mapping(source = "currency", target = "currencyId", qualifiedByName = "mapCurrencyLabelToCurrencyId")
    @Mapping(source = "salaryType", target = "salaryType", qualifiedByName = "mapSalaryTypeToEnumId")
    WorkTripEntity toEntity(WorkTripDTO dto);

    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "employeeName", target = "employee.name")
    WorkTripDTO toDTO(WorkTripRecordEntity recordEntity);

    @Named("mapCurrencyIdToCurrencyLabel")
    static String mapCurrencyIdToCurrencyLabel(Integer currencyId) {
        return Currency.findById(currencyId).label();
    }

    @Named("mapCurrencyLabelToCurrencyId")
    static Integer mapCurrencyLabelToCurrencyId(String label) {
        return Currency.findByLabel(label).id();
    }

    @Named("mapSalaryTypeToEnumName")
    static String mapSalaryTypeToEnumName(Integer typeId) {
        return SalaryType.findById(typeId).label();
    }

    @Named("mapSalaryTypeToEnumId")
    static Integer mapSalaryTypeToEnumId(String name) {
        return SalaryType.findByLabel(name).id();
    }
}
