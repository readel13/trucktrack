/*
 * This file is generated by jOOQ.
 */
package edu.trucktrack.jooq.tables;


import edu.trucktrack.jooq.Public;
import edu.trucktrack.jooq.tables.records.WorkTripSalaryHistoryRecord;

import java.time.LocalDateTime;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.Function5;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Records;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class WorkTripSalaryHistory extends TableImpl<WorkTripSalaryHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.work_trip_salary_history</code>
     */
    public static final WorkTripSalaryHistory WORK_TRIP_SALARY_HISTORY = new WorkTripSalaryHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<WorkTripSalaryHistoryRecord> getRecordType() {
        return WorkTripSalaryHistoryRecord.class;
    }

    /**
     * The column <code>public.work_trip_salary_history.id</code>.
     */
    public final TableField<WorkTripSalaryHistoryRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.work_trip_salary_history.trip_id</code>.
     */
    public final TableField<WorkTripSalaryHistoryRecord, Integer> TRIP_ID = createField(DSL.name("trip_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.work_trip_salary_history.salary_rate</code>.
     */
    public final TableField<WorkTripSalaryHistoryRecord, Float> SALARY_RATE = createField(DSL.name("salary_rate"), SQLDataType.REAL.nullable(false), this, "");

    /**
     * The column
     * <code>public.work_trip_salary_history.calculation_value</code>.
     */
    public final TableField<WorkTripSalaryHistoryRecord, Integer> CALCULATION_VALUE = createField(DSL.name("calculation_value"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.work_trip_salary_history.created_at</code>.
     */
    public final TableField<WorkTripSalaryHistoryRecord, LocalDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.inline("CURRENT_TIMESTAMP", SQLDataType.LOCALDATETIME)), this, "");

    private WorkTripSalaryHistory(Name alias, Table<WorkTripSalaryHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private WorkTripSalaryHistory(Name alias, Table<WorkTripSalaryHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.work_trip_salary_history</code> table
     * reference
     */
    public WorkTripSalaryHistory(String alias) {
        this(DSL.name(alias), WORK_TRIP_SALARY_HISTORY);
    }

    /**
     * Create an aliased <code>public.work_trip_salary_history</code> table
     * reference
     */
    public WorkTripSalaryHistory(Name alias) {
        this(alias, WORK_TRIP_SALARY_HISTORY);
    }

    /**
     * Create a <code>public.work_trip_salary_history</code> table reference
     */
    public WorkTripSalaryHistory() {
        this(DSL.name("work_trip_salary_history"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<WorkTripSalaryHistoryRecord, Long> getIdentity() {
        return (Identity<WorkTripSalaryHistoryRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<WorkTripSalaryHistoryRecord> getPrimaryKey() {
        return Internal.createUniqueKey(WorkTripSalaryHistory.WORK_TRIP_SALARY_HISTORY, DSL.name("work_trip_salary_history_pkey"), new TableField[]{WorkTripSalaryHistory.WORK_TRIP_SALARY_HISTORY.ID}, true);
    }

    @Override
    public WorkTripSalaryHistory as(String alias) {
        return new WorkTripSalaryHistory(DSL.name(alias), this);
    }

    @Override
    public WorkTripSalaryHistory as(Name alias) {
        return new WorkTripSalaryHistory(alias, this);
    }

    @Override
    public WorkTripSalaryHistory as(Table<?> alias) {
        return new WorkTripSalaryHistory(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public WorkTripSalaryHistory rename(String name) {
        return new WorkTripSalaryHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public WorkTripSalaryHistory rename(Name name) {
        return new WorkTripSalaryHistory(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public WorkTripSalaryHistory rename(Table<?> name) {
        return new WorkTripSalaryHistory(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, Integer, Float, Integer, LocalDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function5<? super Long, ? super Integer, ? super Float, ? super Integer, ? super LocalDateTime, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function5<? super Long, ? super Integer, ? super Float, ? super Integer, ? super LocalDateTime, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
