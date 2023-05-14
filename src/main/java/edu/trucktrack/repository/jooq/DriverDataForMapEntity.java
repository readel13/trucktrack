package edu.trucktrack.repository.jooq;

import lombok.Value;

@Value
public class DriverDataForMapEntity {
	Long workTripId;
	String email;
	String driverName;
	String truckName;
	String truckNumber;
	Long companyId;

}
