/*
 * This file is generated by jOOQ.
 */
package edu.trucktrack.dao.jooq.tables.records;

import edu.trucktrack.dao.jooq.tables.WorkTripCargoHistory;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record13;
import org.jooq.Row13;
import org.jooq.impl.UpdatableRecordImpl;

import java.time.LocalDateTime;

/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class WorkTripCargoHistoryRecord extends UpdatableRecordImpl<WorkTripCargoHistoryRecord> implements Record13<Long, Integer, String, String, String, Integer, Integer, LocalDateTime, LocalDateTime, String, LocalDateTime, String, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.work_trip_cargo_history.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.work_trip_cargo_history.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.work_trip_cargo_history.trip_id</code>.
     */
    public void setTripId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.work_trip_cargo_history.trip_id</code>.
     */
    public Integer getTripId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.work_trip_cargo_history.cargo_number</code>.
     */
    public void setCargoNumber(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.work_trip_cargo_history.cargo_number</code>.
     */
    public String getCargoNumber() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.work_trip_cargo_history.cargo_name</code>.
     */
    public void setCargoName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.work_trip_cargo_history.cargo_name</code>.
     */
    public String getCargoName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.work_trip_cargo_history.cargo_description</code>.
     */
    public void setCargoDescription(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.work_trip_cargo_history.cargo_description</code>.
     */
    public String getCargoDescription() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.work_trip_cargo_history.cargo_weight</code>.
     */
    public void setCargoWeight(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.work_trip_cargo_history.cargo_weight</code>.
     */
    public Integer getCargoWeight() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>public.work_trip_cargo_history.distance</code>.
     */
    public void setDistance(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.work_trip_cargo_history.distance</code>.
     */
    public Integer getDistance() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>public.work_trip_cargo_history.delivered_at</code>.
     */
    public void setDeliveredAt(LocalDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.work_trip_cargo_history.delivered_at</code>.
     */
    public LocalDateTime getDeliveredAt() {
        return (LocalDateTime) get(7);
    }

    /**
     * Setter for <code>public.work_trip_cargo_history.created_at</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.work_trip_cargo_history.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(8);
    }

    /**
     * Setter for <code>public.work_trip_cargo_history.loading_location</code>.
     */
    public void setLoadingLocation(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.work_trip_cargo_history.loading_location</code>.
     */
    public String getLoadingLocation() {
        return (String) get(9);
    }

    /**
     * Setter for <code>public.work_trip_cargo_history.loading_time</code>.
     */
    public void setLoadingTime(LocalDateTime value) {
        set(10, value);
    }

    /**
     * Getter for <code>public.work_trip_cargo_history.loading_time</code>.
     */
    public LocalDateTime getLoadingTime() {
        return (LocalDateTime) get(10);
    }

    /**
     * Setter for
     * <code>public.work_trip_cargo_history.unloading_location</code>.
     */
    public void setUnloadingLocation(String value) {
        set(11, value);
    }

    /**
     * Getter for
     * <code>public.work_trip_cargo_history.unloading_location</code>.
     */
    public String getUnloadingLocation() {
        return (String) get(11);
    }

    /**
     * Setter for <code>public.work_trip_cargo_history.unloading_time</code>.
     */
    public void setUnloadingTime(LocalDateTime value) {
        set(12, value);
    }

    /**
     * Getter for <code>public.work_trip_cargo_history.unloading_time</code>.
     */
    public LocalDateTime getUnloadingTime() {
        return (LocalDateTime) get(12);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record13 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row13<Long, Integer, String, String, String, Integer, Integer, LocalDateTime, LocalDateTime, String, LocalDateTime, String, LocalDateTime> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    @Override
    public Row13<Long, Integer, String, String, String, Integer, Integer, LocalDateTime, LocalDateTime, String, LocalDateTime, String, LocalDateTime> valuesRow() {
        return (Row13) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY.ID;
    }

    @Override
    public Field<Integer> field2() {
        return WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY.TRIP_ID;
    }

    @Override
    public Field<String> field3() {
        return WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY.CARGO_NUMBER;
    }

    @Override
    public Field<String> field4() {
        return WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY.CARGO_NAME;
    }

    @Override
    public Field<String> field5() {
        return WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY.CARGO_DESCRIPTION;
    }

    @Override
    public Field<Integer> field6() {
        return WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY.CARGO_WEIGHT;
    }

    @Override
    public Field<Integer> field7() {
        return WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY.DISTANCE;
    }

    @Override
    public Field<LocalDateTime> field8() {
        return WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY.DELIVERED_AT;
    }

    @Override
    public Field<LocalDateTime> field9() {
        return WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY.CREATED_AT;
    }

    @Override
    public Field<String> field10() {
        return WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY.LOADING_LOCATION;
    }

    @Override
    public Field<LocalDateTime> field11() {
        return WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY.LOADING_TIME;
    }

    @Override
    public Field<String> field12() {
        return WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY.UNLOADING_LOCATION;
    }

    @Override
    public Field<LocalDateTime> field13() {
        return WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY.UNLOADING_TIME;
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
    public String component3() {
        return getCargoNumber();
    }

    @Override
    public String component4() {
        return getCargoName();
    }

    @Override
    public String component5() {
        return getCargoDescription();
    }

    @Override
    public Integer component6() {
        return getCargoWeight();
    }

    @Override
    public Integer component7() {
        return getDistance();
    }

    @Override
    public LocalDateTime component8() {
        return getDeliveredAt();
    }

    @Override
    public LocalDateTime component9() {
        return getCreatedAt();
    }

    @Override
    public String component10() {
        return getLoadingLocation();
    }

    @Override
    public LocalDateTime component11() {
        return getLoadingTime();
    }

    @Override
    public String component12() {
        return getUnloadingLocation();
    }

    @Override
    public LocalDateTime component13() {
        return getUnloadingTime();
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
    public String value3() {
        return getCargoNumber();
    }

    @Override
    public String value4() {
        return getCargoName();
    }

    @Override
    public String value5() {
        return getCargoDescription();
    }

    @Override
    public Integer value6() {
        return getCargoWeight();
    }

    @Override
    public Integer value7() {
        return getDistance();
    }

    @Override
    public LocalDateTime value8() {
        return getDeliveredAt();
    }

    @Override
    public LocalDateTime value9() {
        return getCreatedAt();
    }

    @Override
    public String value10() {
        return getLoadingLocation();
    }

    @Override
    public LocalDateTime value11() {
        return getLoadingTime();
    }

    @Override
    public String value12() {
        return getUnloadingLocation();
    }

    @Override
    public LocalDateTime value13() {
        return getUnloadingTime();
    }

    @Override
    public WorkTripCargoHistoryRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public WorkTripCargoHistoryRecord value2(Integer value) {
        setTripId(value);
        return this;
    }

    @Override
    public WorkTripCargoHistoryRecord value3(String value) {
        setCargoNumber(value);
        return this;
    }

    @Override
    public WorkTripCargoHistoryRecord value4(String value) {
        setCargoName(value);
        return this;
    }

    @Override
    public WorkTripCargoHistoryRecord value5(String value) {
        setCargoDescription(value);
        return this;
    }

    @Override
    public WorkTripCargoHistoryRecord value6(Integer value) {
        setCargoWeight(value);
        return this;
    }

    @Override
    public WorkTripCargoHistoryRecord value7(Integer value) {
        setDistance(value);
        return this;
    }

    @Override
    public WorkTripCargoHistoryRecord value8(LocalDateTime value) {
        setDeliveredAt(value);
        return this;
    }

    @Override
    public WorkTripCargoHistoryRecord value9(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public WorkTripCargoHistoryRecord value10(String value) {
        setLoadingLocation(value);
        return this;
    }

    @Override
    public WorkTripCargoHistoryRecord value11(LocalDateTime value) {
        setLoadingTime(value);
        return this;
    }

    @Override
    public WorkTripCargoHistoryRecord value12(String value) {
        setUnloadingLocation(value);
        return this;
    }

    @Override
    public WorkTripCargoHistoryRecord value13(LocalDateTime value) {
        setUnloadingTime(value);
        return this;
    }

    @Override
    public WorkTripCargoHistoryRecord values(Long value1, Integer value2, String value3, String value4, String value5, Integer value6, Integer value7, LocalDateTime value8, LocalDateTime value9, String value10, LocalDateTime value11, String value12, LocalDateTime value13) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached WorkTripCargoHistoryRecord
     */
    public WorkTripCargoHistoryRecord() {
        super(WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY);
    }

    /**
     * Create a detached, initialised WorkTripCargoHistoryRecord
     */
    public WorkTripCargoHistoryRecord(Long id, Integer tripId, String cargoNumber, String cargoName, String cargoDescription, Integer cargoWeight, Integer distance, LocalDateTime deliveredAt, LocalDateTime createdAt, String loadingLocation, LocalDateTime loadingTime, String unloadingLocation, LocalDateTime unloadingTime) {
        super(WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY);

        setId(id);
        setTripId(tripId);
        setCargoNumber(cargoNumber);
        setCargoName(cargoName);
        setCargoDescription(cargoDescription);
        setCargoWeight(cargoWeight);
        setDistance(distance);
        setDeliveredAt(deliveredAt);
        setCreatedAt(createdAt);
        setLoadingLocation(loadingLocation);
        setLoadingTime(loadingTime);
        setUnloadingLocation(unloadingLocation);
        setUnloadingTime(unloadingTime);
    }
}
