package edu.trucktrack.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class CostDTO {

    private Long id;

    @NotBlank
    private String name;

    private String description;

    private String tripName;

    private BigDecimal value;

    private String valueCurrency;

    private Set<String> badges;

    private LocalDateTime createdAt;
}
