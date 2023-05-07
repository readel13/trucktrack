package edu.trucktrack.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Stream;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum Currency {

    USD(1, "USD"), UAH(2, "UAH"), EUR(3, "EUR");

    private final Integer id;
    private final String label;

    public static Currency findById(Integer id){
        return Stream.of(Currency.values())
                .filter(c -> c.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found currency type with id: '%d'".formatted(id)));
    }

    public static Currency findByLabel(String label){
        return Stream.of(Currency.values())
                .filter(c -> c.label.equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found currency type with label: '%s'".formatted(label)));
    }

    public static List<String> getLabels() {
        return Stream.of(Currency.values()).map(Currency::label).toList();
    }
}
