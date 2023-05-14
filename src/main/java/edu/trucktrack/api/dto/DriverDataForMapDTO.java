package edu.trucktrack.api.dto;

import edu.trucktrack.repository.jooq.DriverDataForMapEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DriverDataForMapDTO {
	private DriverDataForMapEntity driverDataForMap;
	private List<String> cargoNames;
	private AndroidTrackerLocationData locationData;

}
