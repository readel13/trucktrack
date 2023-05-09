package edu.trucktrack.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class EmployeeExpensesDTO {

    private Long id;

    private String name;

    private String description;

    private SimpleEmployeeDTO employee;

    private WorkTripDTO trip;

    private Long value;

    private String currency;

    private Set<TagDTO> tags;

    private LocalDateTime createdAt;
}
