package edu.trucktrack.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AndroidTrackerLocationData {

	private String email;
	private Double latitude;
	private Double longitude;
	private Double speed;
}
