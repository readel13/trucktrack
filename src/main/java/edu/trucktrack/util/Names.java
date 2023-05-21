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
    Name SUB_EMP = name("sub_emp");
    Name CMP = name("cmp");
    Name WSH = name("wsh");
    Name EXPS = name("exps");

    // field aliases
    Name ID = name("id");

    Name NAME = name("name");
    Name IS_SYSTEM = name("is_system");
    Name COMPANY_ID = name("company_id");
    Name COMPANY_NAME = name("company_name");
    Name EXPENSE_ID = name("expense_id");
    Name TRIP_ID = name("trip_id");
    Name TRIP_NAME = name("trip_name");
    Name CALCULATION_VALUE = name("calculation_value");
    Name TRUCK_ID = name("truck_id");
    Name TRUCK_NAME = name("truck_name");
    Name TRUCK_NUMBER = name("truck_number");
    Name VIN_CODE = name("vin_code");
    Name FUEL_CONSUMPTION = name("fuel_consumption");
    Name DESCRIPTION = name("description");
    Name EMPLOYEE_ID = name("employee_id");
    Name EMPLOYEE_NAME = name("employee_name");
    Name SALARY = name("salary");
    Name SALARY_RATE = name("salary_rate");
    Name SALARY_TYPE = name("salary_type");
    Name CURRENCY = name("currency");
    Name ACTIVE = name("active");
    Name AVAILABLE = name("available");
    Name VALUE = name("value");

    Name CREATED_AT = name("created_at");
    Name CREATED_BY_EMPLOYEE_ID = name("created_by_employee_id");
    Name CLOSED_AT = name("closed_at");
    Name EMPLOYEE_ROLES = name("employee_roles");
    Name ROLES = name("roles");
    Name EMAIL = name("email");

    Name COMPANY = name("company");

    Name CARGO = name("work_trip_cargo_history");

    Name CARGO_NAME = name("cargo_name");

    Name CARGO_DESCRIPTION = name("cargo_description");

    Name CARGO_WEIGHT = name("cargo_weight");

    Name DISTANCE = name("distance");

    Name LOADING_LOCATION = name("loading_location");

    Name LOADING_TIME = name("loading_time");

    Name UNLOADING_LOCATION = name("unloading_location");

    Name UNLOADING_TIME = name("unloading_time");

    Name DELIVERED_AT = name("delivered_at");

    Name EMPLOYEE = name("employee");

    Name WORK_TRIP_ID = name("work_trip_id");
}
