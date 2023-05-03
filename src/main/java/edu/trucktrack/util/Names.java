package edu.trucktrack.util;

import org.jooq.Name;

import static org.jooq.impl.DSL.name;

public interface Names {

    // table aliases
    Name TRIP = name("trip");
    Name TRUCK = name("truck");
    Name EMP = name("emp");

    // field aliases
    Name ID = name("id");
    Name NAME = name("name");
    Name DESCRIPTION = name("description");
    Name EMPLOYEE_ID = name("employee_id");
    Name EMPLOYEE_NAME = name("employee_name");
    Name SALARY = name("salary");
    Name CURRENCY_ID = name("currency_id");
    Name ACTIVE = name("active");
    Name CREATED_AT = name("created_at");
    Name CLOSED_AT = name("closed_at");
}
