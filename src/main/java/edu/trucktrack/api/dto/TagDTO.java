package edu.trucktrack.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class TagDTO {

    private Long id;

    private Integer expenseId;

    private String name;

    private Boolean isSystem;

    private Integer createdByEmployeeId;

    private LocalDateTime createdAt;
}
