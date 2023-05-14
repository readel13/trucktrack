package edu.trucktrack.api.dto;

import edu.trucktrack.dao.entity.CurrencyEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class EmployeeDTO {

    private Long id;

    private String name;

    private String email;

    private String password;

    private String phoneNumber;

    private CurrencyEntity currency;

    private LocalDateTime createdAt;
}
