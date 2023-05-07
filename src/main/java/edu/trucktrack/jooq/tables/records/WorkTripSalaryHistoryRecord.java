/*
 * This file is generated by jOOQ.
 */
package edu.trucktrack.jooq.tables.records;


import edu.trucktrack.jooq.tables.WorkTripSalaryHistory;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class WorkTripSalaryHistoryRecord extends UpdatableRecordImpl<WorkTripSalaryHistoryRecord> implements Record5<Long, Integer, Float, Integer, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.work_trip_salary_history.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.work_trip_salary_history.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.work_trip_salary_history.trip_id</code>.
     */
    public void setTripId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.work_trip_salary_history.trip_id</code>.
     */
    public Integer getTripId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.work_trip_salary_history.salary_rate</code>.
     */
    public void setSalaryRate(Float value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.work_trip_salary_history.salary_rate</code>.
     */
    public Float getSalaryRate() {
        return (Float) get(2);
    }

    /**
     * Setter for
     * <code>public.work_trip_salary_history.calculation_value</code>.
     */
    public void setCalculationValue(Integer value) {
        set(3, value);
    }

    /**
     * Getter for
     * <code>public.work_trip_salary_history.calculation_value</code>.
     */
    public Integer getCalculationValue() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>public.work_trip_salary_history.created_at</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.work_trip_salary_history.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, Integer, Float, Integer, LocalDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, Integer, Float, Integer, LocalDateTime> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return WorkTripSalaryHistory.WORK_TRIP_SALARY_HISTORY.ID;
    }

    @Override
    public Field<Integer> field2() {
        return WorkTripSalaryHistory.WORK_TRIP_SALARY_HISTORY.TRIP_ID;
    }

    @Override
    public Field<Float> field3() {
        return WorkTripSalaryHistory.WORK_TRIP_SALARY_HISTORY.SALARY_RATE;
    }

    @Override
    public Field<Integer> field4() {
        return WorkTripSalaryHistory.WORK_TRIP_SALARY_HISTORY.CALCULATION_VALUE;
    }

    @Override
    public Field<LocalDateTime> field5() {
        return WorkTripSalaryHistory.WORK_TRIP_SALARY_HISTORY.CREATED_AT;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Integer component2() {
        return getTripId();
    }

    @Override
    public Float component3() {
        return getSalaryRate();
    }

    @Override
    public Integer component4() {
        return getCalculationValue();
    }

    @Override
    public LocalDateTime component5() {
        return getCreatedAt();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Integer value2() {
        return getTripId();
    }

    @Override
    public Float value3() {
        return getSalaryRate();
    }

    @Override
    public Integer value4() {
        return getCalculationValue();
    }

    @Override
    public LocalDateTime value5() {
        return getCreatedAt();
    }

    @Override
    public WorkTripSalaryHistoryRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public WorkTripSalaryHistoryRecord value2(Integer value) {
        setTripId(value);
        return this;
    }

    @Override
    public WorkTripSalaryHistoryRecord value3(Float value) {
        setSalaryRate(value);
        return this;
    }

    @Override
    public WorkTripSalaryHistoryRecord value4(Integer value) {
        setCalculationValue(value);
        return this;
    }

    @Override
    public WorkTripSalaryHistoryRecord value5(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public WorkTripSalaryHistoryRecord values(Long value1, Integer value2, Float value3, Integer value4, LocalDateTime value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached WorkTripSalaryHistoryRecord
     */
    public WorkTripSalaryHistoryRecord() {
        super(WorkTripSalaryHistory.WORK_TRIP_SALARY_HISTORY);
    }

    /**
     * Create a detached, initialised WorkTripSalaryHistoryRecord
     */
    public WorkTripSalaryHistoryRecord(Long id, Integer tripId, Float salaryRate, Integer calculationValue, LocalDateTime createdAt) {
        super(WorkTripSalaryHistory.WORK_TRIP_SALARY_HISTORY);

        setId(id);
        setTripId(tripId);
        setSalaryRate(salaryRate);
        setCalculationValue(calculationValue);
        setCreatedAt(createdAt);
    }
}
