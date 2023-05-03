package edu.trucktrack.api.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class FilterBy {

    private Integer companyId;

    private Integer employeeId;
}
