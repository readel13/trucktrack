package edu.trucktrack.dao.repository.jooq;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeEntityRecord {
	private Long id;
	private String name;
	private String email;
	private Long tripId;
}
