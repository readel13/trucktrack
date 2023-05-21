package edu.trucktrack.dao.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "employee_expenses")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmployeeExpensesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "employee_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EmployeeEntity employee;

    @JoinColumn(name = "trip_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private WorkTripEntity trip;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_expenses_tag",
            joinColumns = { @JoinColumn(name = "expense_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    private Set<TagEntity> tags = new HashSet<>();

    private String name;

    private String description;

    private Integer value;

    private Integer currencyId;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EmployeeExpensesEntity that = (EmployeeExpensesEntity) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(employee, that.employee)
                .append(name, that.name)
                .append(description, that.description)
                .append(value, that.value)
                .append(createdAt, that.createdAt)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(employee)
                .append(name)
                .append(description)
                .append(value)
                .append(createdAt)
                .toHashCode();
    }
}