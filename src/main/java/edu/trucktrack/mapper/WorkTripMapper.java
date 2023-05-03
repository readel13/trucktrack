package edu.trucktrack.mapper;

import edu.trucktrack.api.dto.WorkTripDTO;
import edu.trucktrack.entity.WorkTripEntity;
import edu.trucktrack.repository.jooq.WorkTripRecordEntity;
import org.mapstruct.Mapper;

@Mapper
public interface WorkTripMapper {

    WorkTripDTO toDTO(WorkTripEntity entity);

    WorkTripEntity toEntity(WorkTripDTO dto);

    WorkTripDTO toDTO(WorkTripRecordEntity recordEntity);
}
