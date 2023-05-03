package edu.trucktrack.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class SimpleEmployeeDTO {

    private Integer id;

    private String name;
}
