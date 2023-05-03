package edu.trucktrack.mapper;

import edu.trucktrack.api.dto.TruckDTO;
import edu.trucktrack.entity.TruckEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TruckMapper {

    TruckEntity toEntity(TruckDTO dto);

    @Mapping(target = "companyId", source = "entity.company.id")
    TruckDTO toDTO(TruckEntity entity);
}
