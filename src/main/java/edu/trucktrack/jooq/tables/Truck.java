/*
 * This file is generated by jOOQ.
 */
package edu.trucktrack.jooq.tables;


import edu.trucktrack.jooq.Public;
import edu.trucktrack.jooq.tables.records.TruckRecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.Function8;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.OrderField;
import org.jooq.Records;
import org.jooq.Row8;
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
public class Truck extends TableImpl<TruckRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.truck</code>
     */
    public static final Truck TRUCK = new Truck();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TruckRecord> getRecordType() {
        return TruckRecord.class;
    }

    /**
     * The column <code>public.truck.id</code>.
     */
    public final TableField<TruckRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.truck.name</code>.
     */
    public final TableField<TruckRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>public.truck.truck_number</code>.
     */
    public final TableField<TruckRecord, String> TRUCK_NUMBER = createField(DSL.name("truck_number"), SQLDataType.VARCHAR(64), this, "");

    /**
     * The column <code>public.truck.vin_code</code>.
     */
    public final TableField<TruckRecord, String> VIN_CODE = createField(DSL.name("vin_code"), SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>public.truck.fuel_consumption</code>.
     */
    public final TableField<TruckRecord, BigDecimal> FUEL_CONSUMPTION = createField(DSL.name("fuel_consumption"), SQLDataType.NUMERIC(4, 2), this, "");

    /**
     * The column <code>public.truck.active</code>.
     */
    public final TableField<TruckRecord, Boolean> ACTIVE = createField(DSL.name("active"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.inline("true", SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.truck.created_at</code>.
     */
    public final TableField<TruckRecord, LocalDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.inline("CURRENT_TIMESTAMP", SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>public.truck.company_id</code>.
     */
    public final TableField<TruckRecord, Integer> COMPANY_ID = createField(DSL.name("company_id"), SQLDataType.INTEGER, this, "");

    private Truck(Name alias, Table<TruckRecord> aliased) {
        this(alias, aliased, null);
    }

    private Truck(Name alias, Table<TruckRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.truck</code> table reference
     */
    public Truck(String alias) {
        this(DSL.name(alias), TRUCK);
    }

    /**
     * Create an aliased <code>public.truck</code> table reference
     */
    public Truck(Name alias) {
        this(alias, TRUCK);
    }

    /**
     * Create a <code>public.truck</code> table reference
     */
    public Truck() {
        this(DSL.name("truck"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(
            Internal.createIndex(DSL.name("idx_truck_name"), Truck.TRUCK, new OrderField[] { Truck.TRUCK.NAME }, false), 
            Internal.createIndex(DSL.name("idx_truck_vin_code"), Truck.TRUCK, new OrderField[] { Truck.TRUCK.VIN_CODE }, true)
        );
    }

    @Override
    public Identity<TruckRecord, Long> getIdentity() {
        return (Identity<TruckRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<TruckRecord> getPrimaryKey() {
        return Internal.createUniqueKey(Truck.TRUCK, DSL.name("truck_pkey"), new TableField[] { Truck.TRUCK.ID }, true);
    }

    @Override
    public Truck as(String alias) {
        return new Truck(DSL.name(alias), this);
    }

    @Override
    public Truck as(Name alias) {
        return new Truck(alias, this);
    }

    @Override
    public Truck as(Table<?> alias) {
        return new Truck(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Truck rename(String name) {
        return new Truck(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Truck rename(Name name) {
        return new Truck(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Truck rename(Table<?> name) {
        return new Truck(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row8 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row8<Long, String, String, String, BigDecimal, Boolean, LocalDateTime, Integer> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function8<? super Long, ? super String, ? super String, ? super String, ? super BigDecimal, ? super Boolean, ? super LocalDateTime, ? super Integer, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function8<? super Long, ? super String, ? super String, ? super String, ? super BigDecimal, ? super Boolean, ? super LocalDateTime, ? super Integer, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
