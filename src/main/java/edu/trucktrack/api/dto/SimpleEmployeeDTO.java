package edu.trucktrack.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class SimpleEmployeeDTO {

    private Long id;

    private String name;
}
