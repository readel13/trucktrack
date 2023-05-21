package edu.trucktrack.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@Jacksonized
@NoArgsConstructor
public class AndroidTrackerLocationData {

	private String email;
	private Double latitude;
	private Double longitude;
	private Double speed;
}
