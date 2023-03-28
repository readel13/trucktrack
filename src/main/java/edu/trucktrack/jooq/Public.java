/*
 * This file is generated by jOOQ.
 */
package edu.trucktrack.jooq;


import edu.trucktrack.jooq.tables.*;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.company</code>.
     */
    public final Company COMPANY = Company.COMPANY;

    /**
     * The table <code>public.currency</code>.
     */
    public final Currency CURRENCY = Currency.CURRENCY;


    /**
     * The table <code>public.employee</code>.
     */
    public final Employee EMPLOYEE = Employee.EMPLOYEE;

    /**
     * The table <code>public.employee_expenses</code>.
     */
    public final EmployeeExpenses EMPLOYEE_EXPENSES = EmployeeExpenses.EMPLOYEE_EXPENSES;

    /**
     * The table <code>public.employee_expenses_tag</code>.
     */
    public final EmployeeExpensesTag EMPLOYEE_EXPENSES_TAG = EmployeeExpensesTag.EMPLOYEE_EXPENSES_TAG;

    /**
     * The table <code>public.salary_type</code>.
     */
    public final SalaryType SALARY_TYPE = SalaryType.SALARY_TYPE;

    /**
     * The table <code>public.tag</code>.
     */
    public final Tag TAG = Tag.TAG;

    /**
     * The table <code>public.truck</code>.
     */
    public final Truck TRUCK = Truck.TRUCK;

    /**
     * The table <code>public.work_trip</code>.
     */
    public final WorkTrip WORK_TRIP = WorkTrip.WORK_TRIP;

    /**
     * The table <code>public.work_trip_cargo_history</code>.
     */
    public final WorkTripCargoHistory WORK_TRIP_CARGO_HISTORY = WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY;

    /**
     * The table <code>public.work_trip_salary_history</code>.
     */
    public final WorkTripSalaryHistory WORK_TRIP_SALARY_HISTORY = WorkTripSalaryHistory.WORK_TRIP_SALARY_HISTORY;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
                Company.COMPANY,
                Currency.CURRENCY,
                Employee.EMPLOYEE,
                EmployeeExpenses.EMPLOYEE_EXPENSES,
                EmployeeExpensesTag.EMPLOYEE_EXPENSES_TAG,
                SalaryType.SALARY_TYPE,
                Tag.TAG,
                Truck.TRUCK,
                WorkTrip.WORK_TRIP,
                WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY,
                WorkTripSalaryHistory.WORK_TRIP_SALARY_HISTORY
        );
    }
}
