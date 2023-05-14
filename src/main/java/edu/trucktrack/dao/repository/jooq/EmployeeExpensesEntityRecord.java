package edu.trucktrack.dao.repository.jooq;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class EmployeeExpensesEntityRecord {
    private Long id;

    private String name;

    private String description;

    private Long employeeId;

    private String employeeName;

    private Long tripId;

    private String tripName;

    private Long value;

    private String currency;

    private LocalDateTime createdAt;
}
