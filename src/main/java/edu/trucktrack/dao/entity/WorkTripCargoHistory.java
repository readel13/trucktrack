package edu.trucktrack.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "work_trip_cargo_history")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkTripCargoHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "trip_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private WorkTripEntity trip;

    private String cargoNumber;

    private String cargoName;

    private String cargoDescription;

    private Integer cargoWeight;

    private Integer distance;

    private String loadingLocation;

    private LocalDateTime loadingTime;

    private String unloadingLocation;

    private LocalDateTime unloadingTime;

    private LocalDateTime deliveredAt;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        WorkTripCargoHistory that = (WorkTripCargoHistory) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(cargoNumber, that.cargoNumber)
                .append(cargoName, that.cargoName)
                .append(cargoDescription, that.cargoDescription)
                .append(cargoWeight, that.cargoWeight)
                .append(distance, that.distance)
                .append(deliveredAt, that.deliveredAt)
                .append(createdAt, that.createdAt)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(cargoNumber)
                .append(cargoName)
                .append(cargoDescription)
                .append(cargoWeight)
                .append(distance)
                .append(deliveredAt)
                .append(createdAt)
                .toHashCode();
    }
}