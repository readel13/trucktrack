package edu.trucktrack.dao.repository.jooq;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class WorkTripRecordEntity {

    private Long id;

    private String name;

    private String description;

    private Integer employeeId;

    private String employeeName;

    private Long truckId;

    private String truckName;

    private BigDecimal salary;

    private String salaryType;

    private Double salaryRate;

    private String currency;

    private boolean active;

    private LocalDateTime closedAt;

    private LocalDateTime createdAt;
}
