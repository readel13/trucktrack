package edu.trucktrack.mapper;

import edu.trucktrack.api.dto.CompanyDTO;
import edu.trucktrack.dao.entity.CompanyEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CompanyMapper {
    CompanyEntity toEntity(CompanyDTO dto);

    CompanyDTO toDTO(CompanyEntity company);
}
