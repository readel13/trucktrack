package edu.trucktrack.mapper;

import edu.trucktrack.api.dto.EmployeeExpensesDTO;
import edu.trucktrack.repository.jooq.EmployeeExpensesEntityRecord;
import org.mapstruct.Mapper;

@Mapper
public interface EmployeeExpensesMapper {

    EmployeeExpensesDTO toDTO(EmployeeExpensesEntityRecord entityRecord);
}
