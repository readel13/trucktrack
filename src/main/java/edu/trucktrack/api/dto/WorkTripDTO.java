package edu.trucktrack.api.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class WorkTripDTO {

    private Long id;

    @NotBlank
    private String name;

    private String description;

    private SimpleEmployeeDTO employeeDTO;

    private Integer salary;

    private Integer currencyId;

    private boolean active;

    private LocalDateTime closedAt;

    private LocalDateTime createdAt;
}
