/*
 * This file is generated by jOOQ.
 */
package edu.trucktrack.dao.jooq.tables;



import java.time.LocalDateTime;
import java.util.function.Function;

import edu.trucktrack.dao.jooq.Public;
import edu.trucktrack.dao.jooq.tables.records.WorkTripCargoHistoryRecord;
import org.jooq.Field;
import org.jooq.Function13;
import org.jooq.Function9;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Records;
import org.jooq.Row13;
import org.jooq.Row9;
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
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class WorkTripCargoHistory extends TableImpl<WorkTripCargoHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.work_trip_cargo_history</code>
     */
    public static final WorkTripCargoHistory WORK_TRIP_CARGO_HISTORY = new WorkTripCargoHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<WorkTripCargoHistoryRecord> getRecordType() {
        return WorkTripCargoHistoryRecord.class;
    }

    /**
     * The column <code>public.work_trip_cargo_history.id</code>.
     */
    public final TableField<WorkTripCargoHistoryRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.work_trip_cargo_history.trip_id</code>.
     */
    public final TableField<WorkTripCargoHistoryRecord, Integer> TRIP_ID = createField(DSL.name("trip_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.work_trip_cargo_history.cargo_number</code>.
     */
    public final TableField<WorkTripCargoHistoryRecord, String> CARGO_NUMBER = createField(DSL.name("cargo_number"), SQLDataType.VARCHAR(64), this, "");

    /**
     * The column <code>public.work_trip_cargo_history.cargo_name</code>.
     */
    public final TableField<WorkTripCargoHistoryRecord, String> CARGO_NAME = createField(DSL.name("cargo_name"), SQLDataType.VARCHAR(32), this, "");

    /**
     * The column <code>public.work_trip_cargo_history.cargo_description</code>.
     */
    public final TableField<WorkTripCargoHistoryRecord, String> CARGO_DESCRIPTION = createField(DSL.name("cargo_description"), SQLDataType.VARCHAR(32), this, "");

    /**
     * The column <code>public.work_trip_cargo_history.cargo_weight</code>.
     */
    public final TableField<WorkTripCargoHistoryRecord, Integer> CARGO_WEIGHT = createField(DSL.name("cargo_weight"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.work_trip_cargo_history.distance</code>.
     */
    public final TableField<WorkTripCargoHistoryRecord, Integer> DISTANCE = createField(DSL.name("distance"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.work_trip_cargo_history.delivered_at</code>.
     */
    public final TableField<WorkTripCargoHistoryRecord, LocalDateTime> DELIVERED_AT = createField(DSL.name("delivered_at"), SQLDataType.LOCALDATETIME(6), this, "");

    /**
     * The column <code>public.work_trip_cargo_history.created_at</code>.
     */
    public final TableField<WorkTripCargoHistoryRecord, LocalDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.inline("CURRENT_TIMESTAMP", SQLDataType.LOCALDATETIME)), this, "");
    /**
     * The column <code>public.work_trip_cargo_history.loading_location</code>.
     */
    public final TableField<WorkTripCargoHistoryRecord, String> LOADING_LOCATION = createField(DSL.name("loading_location"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>public.work_trip_cargo_history.loading_time</code>.
     */
    public final TableField<WorkTripCargoHistoryRecord, LocalDateTime> LOADING_TIME = createField(DSL.name("loading_time"), SQLDataType.LOCALDATETIME(6), this, "");

    /**
     * The column
     * <code>public.work_trip_cargo_history.unloading_location</code>.
     */
    public final TableField<WorkTripCargoHistoryRecord, String> UNLOADING_LOCATION = createField(DSL.name("unloading_location"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>public.work_trip_cargo_history.unloading_time</code>.
     */
    public final TableField<WorkTripCargoHistoryRecord, LocalDateTime> UNLOADING_TIME = createField(DSL.name("unloading_time"), SQLDataType.LOCALDATETIME(6), this, "");

    private WorkTripCargoHistory(Name alias, Table<WorkTripCargoHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private WorkTripCargoHistory(Name alias, Table<WorkTripCargoHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.work_trip_cargo_history</code> table
     * reference
     */
    public WorkTripCargoHistory(String alias) {
        this(DSL.name(alias), WORK_TRIP_CARGO_HISTORY);
    }

    /**
     * Create an aliased <code>public.work_trip_cargo_history</code> table
     * reference
     */
    public WorkTripCargoHistory(Name alias) {
        this(alias, WORK_TRIP_CARGO_HISTORY);
    }

    /**
     * Create a <code>public.work_trip_cargo_history</code> table reference
     */
    public WorkTripCargoHistory() {
        this(DSL.name("work_trip_cargo_history"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<WorkTripCargoHistoryRecord, Long> getIdentity() {
        return (Identity<WorkTripCargoHistoryRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<WorkTripCargoHistoryRecord> getPrimaryKey() {
        return Internal.createUniqueKey(WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY, DSL.name("work_trip_cargo_history_pkey"), new TableField[] { WorkTripCargoHistory.WORK_TRIP_CARGO_HISTORY.ID }, true);
    }

    @Override
    public WorkTripCargoHistory as(String alias) {
        return new WorkTripCargoHistory(DSL.name(alias), this);
    }

    @Override
    public WorkTripCargoHistory as(Name alias) {
        return new WorkTripCargoHistory(alias, this);
    }

    @Override
    public WorkTripCargoHistory as(Table<?> alias) {
        return new WorkTripCargoHistory(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public WorkTripCargoHistory rename(String name) {
        return new WorkTripCargoHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public WorkTripCargoHistory rename(Name name) {
        return new WorkTripCargoHistory(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public WorkTripCargoHistory rename(Table<?> name) {
        return new WorkTripCargoHistory(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row13 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row13<Long, Integer, String, String, String, Integer, Integer, LocalDateTime, LocalDateTime, String, LocalDateTime, String, LocalDateTime> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(
            Function13<? super Long, ? super Integer, ? super String, ? super String, ? super String, ? super Integer, ? super Integer, ? super LocalDateTime, ? super LocalDateTime, ? super String, ? super LocalDateTime, ? super String, ? super LocalDateTime, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function13<? super Long, ? super Integer, ? super String, ? super String, ? super String, ? super Integer, ? super Integer, ? super LocalDateTime, ? super LocalDateTime, ? super String, ? super LocalDateTime, ? super String, ? super LocalDateTime, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
