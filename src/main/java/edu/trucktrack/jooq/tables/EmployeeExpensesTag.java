/*
 * This file is generated by jOOQ.
 */
package edu.trucktrack.jooq.tables;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import edu.trucktrack.jooq.Keys;
import edu.trucktrack.jooq.Public;
import edu.trucktrack.jooq.tables.record.EmployeeExpensesTagRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function4;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EmployeeExpensesTag extends TableImpl<EmployeeExpensesTagRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.employee_expenses_tag</code>
     */
    public static final EmployeeExpensesTag EMPLOYEE_EXPENSES_TAG = new EmployeeExpensesTag();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EmployeeExpensesTagRecord> getRecordType() {
        return EmployeeExpensesTagRecord.class;
    }

    /**
     * The column <code>public.employee_expenses_tag.id</code>.
     */
    public final TableField<EmployeeExpensesTagRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.employee_expenses_tag.expense_id</code>.
     */
    public final TableField<EmployeeExpensesTagRecord, Integer> EXPENSE_ID = createField(DSL.name("expense_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.employee_expenses_tag.tag_id</code>.
     */
    public final TableField<EmployeeExpensesTagRecord, Integer> TAG_ID = createField(DSL.name("tag_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.employee_expenses_tag.created_at</code>.
     */
    public final TableField<EmployeeExpensesTagRecord, LocalDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.inline("CURRENT_TIMESTAMP", SQLDataType.LOCALDATETIME)), this, "");

    private EmployeeExpensesTag(Name alias, Table<EmployeeExpensesTagRecord> aliased) {
        this(alias, aliased, null);
    }

    private EmployeeExpensesTag(Name alias, Table<EmployeeExpensesTagRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.employee_expenses_tag</code> table
     * reference
     */
    public EmployeeExpensesTag(String alias) {
        this(DSL.name(alias), EMPLOYEE_EXPENSES_TAG);
    }

    /**
     * Create an aliased <code>public.employee_expenses_tag</code> table
     * reference
     */
    public EmployeeExpensesTag(Name alias) {
        this(alias, EMPLOYEE_EXPENSES_TAG);
    }

    /**
     * Create a <code>public.employee_expenses_tag</code> table reference
     */
    public EmployeeExpensesTag() {
        this(DSL.name("employee_expenses_tag"), null);
    }

    public <O extends Record> EmployeeExpensesTag(Table<O> child, ForeignKey<O, EmployeeExpensesTagRecord> key) {
        super(child, key, EMPLOYEE_EXPENSES_TAG);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<EmployeeExpensesTagRecord, Long> getIdentity() {
        return (Identity<EmployeeExpensesTagRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<EmployeeExpensesTagRecord> getPrimaryKey() {
        return Keys.EMPLOYEE_EXPENSES_TAG_PKEY;
    }

    @Override
    public List<ForeignKey<EmployeeExpensesTagRecord, ?>> getReferences() {
        return Arrays.asList(Keys.EMPLOYEE_EXPENSES_TAG__FK_EMPLOYEE_EXPENSES_TAG_EXPENSE_ID, Keys.EMPLOYEE_EXPENSES_TAG__FK_EMPLOYEE_EXPENSES_TAG_TAG_ID);
    }

    private transient EmployeeExpenses _employeeExpenses;
    private transient Tag _tag;

    /**
     * Get the implicit join path to the <code>public.employee_expenses</code>
     * table.
     */
    public EmployeeExpenses employeeExpenses() {
        if (_employeeExpenses == null)
            _employeeExpenses = new EmployeeExpenses(this, Keys.EMPLOYEE_EXPENSES_TAG__FK_EMPLOYEE_EXPENSES_TAG_EXPENSE_ID);

        return _employeeExpenses;
    }

    /**
     * Get the implicit join path to the <code>public.tag</code> table.
     */
    public Tag tag() {
        if (_tag == null)
            _tag = new Tag(this, Keys.EMPLOYEE_EXPENSES_TAG__FK_EMPLOYEE_EXPENSES_TAG_TAG_ID);

        return _tag;
    }

    @Override
    public EmployeeExpensesTag as(String alias) {
        return new EmployeeExpensesTag(DSL.name(alias), this);
    }

    @Override
    public EmployeeExpensesTag as(Name alias) {
        return new EmployeeExpensesTag(alias, this);
    }

    @Override
    public EmployeeExpensesTag as(Table<?> alias) {
        return new EmployeeExpensesTag(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public EmployeeExpensesTag rename(String name) {
        return new EmployeeExpensesTag(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public EmployeeExpensesTag rename(Name name) {
        return new EmployeeExpensesTag(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public EmployeeExpensesTag rename(Table<?> name) {
        return new EmployeeExpensesTag(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, Integer, Integer, LocalDateTime> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super Long, ? super Integer, ? super Integer, ? super LocalDateTime, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super Long, ? super Integer, ? super Integer, ? super LocalDateTime, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
