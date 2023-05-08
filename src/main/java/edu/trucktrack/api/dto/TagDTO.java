package edu.trucktrack.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class TagDTO {

    private Long id;

    private Integer expenseId;

    private String name;
}
