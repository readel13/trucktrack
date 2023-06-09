package edu.trucktrack.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class TruckDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Long companyId;

    private String companyName;

    @NotBlank
    private String truckNumber;

    @NotBlank
    private String vinCode;

    private Double fuelConsumption;

    private boolean active;

    private boolean available;

    private LocalDate createdAt;
}
