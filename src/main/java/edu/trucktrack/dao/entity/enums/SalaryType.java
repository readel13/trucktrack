package edu.trucktrack.dao.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Stream;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum SalaryType {

    HOURLY(1, "HOURLY"), PER_KM(2, "PER_KM");

    private final Integer id;
    private final String label;

    public static SalaryType findByLabel(String label) {
        return Stream.of(SalaryType.values())
                .filter(type -> type.label.equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found salary type with label: '%s'".formatted(label)));
    }

    public static SalaryType findById(Integer id) {
        return Stream.of(SalaryType.values())
                .filter(type -> type.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found salary type with id: '%d'".formatted(id)));
    }

    public static List<String> getLabels() {
        return Stream.of(SalaryType.values()).map(SalaryType::label).toList();
    }
}
