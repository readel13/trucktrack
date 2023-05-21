package edu.trucktrack.api.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class FilterBy {

    private Long id;

    private String name;

    private Long tripId;

    private Integer companyId;

    private Integer employeeId;

    private LocalDate from;

    private LocalDate to;

    private String email;

    private String employeeName;

    private String loadingLocation;

    private String unloadingLocation;
}
