package edu.trucktrack.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CargoDTO {

	private Long id;
	private Long tripId;
	private Long employeeId;
	private String driverName;
	private String driverEmail;
	private String cargoName;
	private String cargoDescription;
	private Integer cargoWeight;
	private Integer distance;
	private String loadingLocation;
	private LocalDateTime loadingTime;
	private String unloadingLocation;
	private LocalDateTime unloadingTime;
	private LocalDateTime createdAt;
	private LocalDateTime deliveredAt;
}

