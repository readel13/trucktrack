package edu.trucktrack.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class CostDTO {

    private Long id;

    @NotBlank
    private String name;

    private String description;

    private BigDecimal amount;

    private String currencyAmount;

    private Set<String> badges;
}
