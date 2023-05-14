/*
 * This file is generated by jOOQ.
 */
package edu.trucktrack.dao.jooq.tables.records;


import edu.trucktrack.dao.jooq.tables.Truck;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TruckRecord extends UpdatableRecordImpl<TruckRecord> implements Record8<Long, String, String, String, BigDecimal, Boolean, LocalDateTime, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.truck.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.truck.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.truck.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.truck.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.truck.truck_number</code>.
     */
    public void setTruckNumber(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.truck.truck_number</code>.
     */
    public String getTruckNumber() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.truck.vin_code</code>.
     */
    public void setVinCode(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.truck.vin_code</code>.
     */
    public String getVinCode() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.truck.fuel_consumption</code>.
     */
    public void setFuelConsumption(BigDecimal value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.truck.fuel_consumption</code>.
     */
    public BigDecimal getFuelConsumption() {
        return (BigDecimal) get(4);
    }

    /**
     * Setter for <code>public.truck.active</code>.
     */
    public void setActive(Boolean value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.truck.active</code>.
     */
    public Boolean getActive() {
        return (Boolean) get(5);
    }

    /**
     * Setter for <code>public.truck.created_at</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.truck.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(6);
    }

    /**
     * Setter for <code>public.truck.company_id</code>.
     */
    public void setCompanyId(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.truck.company_id</code>.
     */
    public Integer getCompanyId() {
        return (Integer) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row8<Long, String, String, String, BigDecimal, Boolean, LocalDateTime, Integer> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<Long, String, String, String, BigDecimal, Boolean, LocalDateTime, Integer> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Truck.TRUCK.ID;
    }

    @Override
    public Field<String> field2() {
        return Truck.TRUCK.NAME;
    }

    @Override
    public Field<String> field3() {
        return Truck.TRUCK.TRUCK_NUMBER;
    }

    @Override
    public Field<String> field4() {
        return Truck.TRUCK.VIN_CODE;
    }

    @Override
    public Field<BigDecimal> field5() {
        return Truck.TRUCK.FUEL_CONSUMPTION;
    }

    @Override
    public Field<Boolean> field6() {
        return Truck.TRUCK.ACTIVE;
    }

    @Override
    public Field<LocalDateTime> field7() {
        return Truck.TRUCK.CREATED_AT;
    }

    @Override
    public Field<Integer> field8() {
        return Truck.TRUCK.COMPANY_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public String component3() {
        return getTruckNumber();
    }

    @Override
    public String component4() {
        return getVinCode();
    }

    @Override
    public BigDecimal component5() {
        return getFuelConsumption();
    }

    @Override
    public Boolean component6() {
        return getActive();
    }

    @Override
    public LocalDateTime component7() {
        return getCreatedAt();
    }

    @Override
    public Integer component8() {
        return getCompanyId();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public String value3() {
        return getTruckNumber();
    }

    @Override
    public String value4() {
        return getVinCode();
    }

    @Override
    public BigDecimal value5() {
        return getFuelConsumption();
    }

    @Override
    public Boolean value6() {
        return getActive();
    }

    @Override
    public LocalDateTime value7() {
        return getCreatedAt();
    }

    @Override
    public Integer value8() {
        return getCompanyId();
    }

    @Override
    public TruckRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public TruckRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public TruckRecord value3(String value) {
        setTruckNumber(value);
        return this;
    }

    @Override
    public TruckRecord value4(String value) {
        setVinCode(value);
        return this;
    }

    @Override
    public TruckRecord value5(BigDecimal value) {
        setFuelConsumption(value);
        return this;
    }

    @Override
    public TruckRecord value6(Boolean value) {
        setActive(value);
        return this;
    }

    @Override
    public TruckRecord value7(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public TruckRecord value8(Integer value) {
        setCompanyId(value);
        return this;
    }

    @Override
    public TruckRecord values(Long value1, String value2, String value3, String value4, BigDecimal value5, Boolean value6, LocalDateTime value7, Integer value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TruckRecord
     */
    public TruckRecord() {
        super(Truck.TRUCK);
    }

    /**
     * Create a detached, initialised TruckRecord
     */
    public TruckRecord(Long id, String name, String truckNumber, String vinCode, BigDecimal fuelConsumption, Boolean active, LocalDateTime createdAt, Integer companyId) {
        super(Truck.TRUCK);

        setId(id);
        setName(name);
        setTruckNumber(truckNumber);
        setVinCode(vinCode);
        setFuelConsumption(fuelConsumption);
        setActive(active);
        setCreatedAt(createdAt);
        setCompanyId(companyId);
    }
}