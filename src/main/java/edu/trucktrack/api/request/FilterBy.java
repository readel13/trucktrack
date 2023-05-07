package edu.trucktrack.api.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class FilterBy {

    private Long id;

    private String name;

    private Long tripId;

    private Integer companyId;

    private Integer employeeId;
}
