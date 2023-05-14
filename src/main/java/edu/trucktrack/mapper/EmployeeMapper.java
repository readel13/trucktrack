package edu.trucktrack.mapper;

import edu.trucktrack.api.dto.EmployeeDTO;
import edu.trucktrack.api.dto.SimpleEmployeeDTO;
import edu.trucktrack.dao.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface EmployeeMapper {

    EmployeeEntity toEntity(EmployeeDTO employeeDTO);

    EmployeeEntity toEntity(SimpleEmployeeDTO simpleEmployeeDTO);
}
