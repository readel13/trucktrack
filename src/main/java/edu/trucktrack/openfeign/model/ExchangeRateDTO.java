package edu.trucktrack.openfeign.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Data
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Accessors(fluent = true)
public class ExchangeRateDTO {
    public static final String RATE_KEY = "rate";
    private Map<String, Double> info;
}
