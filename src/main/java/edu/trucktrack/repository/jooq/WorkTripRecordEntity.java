package edu.trucktrack.repository.jooq;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
public class WorkTripRecordEntity {

    Long id;

    String name;

    String description;

    Integer employeeId;

    String employeeName;

    BigDecimal salary;

    Integer currencyId;

    boolean active;

    LocalDateTime closedAt;

    LocalDateTime createdAt;
}
