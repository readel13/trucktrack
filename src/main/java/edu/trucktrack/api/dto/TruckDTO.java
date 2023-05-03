package edu.trucktrack.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class TruckDTO {

    private Integer id;

    @NotNull
    private Integer companyId;

    @NotBlank
    private String name;

    @NotBlank
    private String truckNumber;

    @NotBlank
    private String vinCode;

    private Double fuelConsumption;

    private boolean active;

    private LocalDate createdAt;
}
