package edu.trucktrack.mapper;

import edu.trucktrack.api.dto.TagDTO;
import edu.trucktrack.entity.TagEntity;
import org.mapstruct.Mapper;

@Mapper
public interface TagMapper {

    TagDTO toDTO(TagEntity entity);

    TagDTO toEntity(TagDTO dto);
}
