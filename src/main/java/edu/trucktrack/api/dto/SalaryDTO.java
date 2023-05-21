package edu.trucktrack.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SalaryDTO {
    private Long id;
    private Integer tripId;
    private String tripName;
    private Long calculationValue;
    private Double salary;
    private String currency;
    private LocalDateTime createdAt;
}
