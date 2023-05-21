package edu.trucktrack.dao.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "employee")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    private String name;

    private String email;

    private String password;

    private Boolean active = false;

    private String phoneNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id")
    private CurrencyEntity currency;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private Set<WorkTripEntity> workTrips = new LinkedHashSet<>();

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private Set<EmployeeExpensesEntity> employeeExpenses = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_roles",
            joinColumns = { @JoinColumn(name = "employee_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    Set<RoleEntity> roles = new HashSet<>();

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EmployeeEntity employee = (EmployeeEntity) o;

        return new EqualsBuilder()
                .append(id, employee.id)
                .append(company, employee.company)
                .append(name, employee.name)
                .append(email, employee.email)
                .append(password, employee.password)
                .append(active, employee.active)
                .append(phoneNumber, employee.phoneNumber)
                .append(currency, employee.currency)
                .append(createdAt, employee.createdAt)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(company)
                .append(name)
                .append(email)
                .append(password)
                .append(active)
                .append(phoneNumber)
                .append(currency)
                .append(createdAt)
                .toHashCode();
    }
}