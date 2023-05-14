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

    private SimpleEmployeeDTO employee;

    private Long truckId;

    private String truckName;

    private Integer salary;

    private Double salaryRate;

    private String salaryType;

    private Integer costs;

    private String currency;

    private boolean active;

    private LocalDateTime closedAt;

    private LocalDateTime createdAt;
}
