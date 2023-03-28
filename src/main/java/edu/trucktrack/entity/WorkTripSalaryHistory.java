package edu.trucktrack.entity;

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
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "work_trip_salary_history")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkTripSalaryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "trip_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private WorkTrip trip;

    @JoinColumn(name = "salary_type")
    @ManyToOne(fetch = FetchType.LAZY)
    private SalaryType salaryType;

    private float salaryRate;

    private Integer calculationValue;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        WorkTripSalaryHistory that = (WorkTripSalaryHistory) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(salaryType, that.salaryType)
                .append(salaryRate, that.salaryRate)
                .append(calculationValue, that.calculationValue)
                .append(createdAt, that.createdAt)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id).append(salaryType)
                .append(salaryRate)
                .append(calculationValue)
                .append(createdAt)
                .toHashCode();
    }
}