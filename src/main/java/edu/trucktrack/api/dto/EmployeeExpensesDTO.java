package edu.trucktrack.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class EmployeeExpensesDTO {

    private Long id;

    private String name;

    private String description;

    private Long tripId;

    private String tripName;

    private Long value;

    private String currency;

    private List<TagDTO> tags;

    private LocalDateTime createdAt;
}
