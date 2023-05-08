package edu.trucktrack.util;

import org.jooq.Name;

import static org.jooq.impl.DSL.name;

public interface Names {

    // table aliases
    Name TRIP = name("trip");
    Name TRUCK = name("truck");
    Name SUB_TRUCK = name("sub_truck");
    Name WORK_TRIP = name("work_trip");
    Name TAG = name("tag");
    Name EET = name("eet");
    Name SLT = name("slt");
    Name CUR = name("cur");
    Name EMP = name("emp");
    Name CMP = name("cmp");
    Name EXPS = name("exps");

    // field aliases
    Name ID = name("id");

    Name NAME = name("name");
    Name COMPANY_ID = name("company_id");
    Name EXPENSE_ID = name("expense_id");
    Name TRIP_ID = name("trip_id");
    Name TRIP_NAME = name("trip_name");
    Name TRUCK_ID = name("truck_id");
    Name TRUCK_NAME = name("truck_name");
    Name TRUCK_NUMBER = name("truck_number");
    Name VIN_CODE = name("vin_code");
    Name FUEL_CONSUMPTION = name("fuel_consumption");
    Name DESCRIPTION = name("description");
    Name EMPLOYEE_ID = name("employee_id");
    Name EMPLOYEE_NAME = name("employee_name");
    Name SALARY = name("salary");
    Name SALARY_TYPE = name("salary_type");
    Name CURRENCY = name("currency");
    Name ACTIVE = name("active");
    Name VALUE = name("value");

    Name CREATED_AT = name("created_at");
    Name CLOSED_AT = name("closed_at");
}
