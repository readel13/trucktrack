/*
 * This file is generated by jOOQ.
 */
package edu.trucktrack.jooq.tables.record;

import java.time.LocalDateTime;

import edu.trucktrack.jooq.tables.EmployeeExpensesTag;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EmployeeExpensesTagRecord extends UpdatableRecordImpl<EmployeeExpensesTagRecord> implements Record4<Long, Integer, Integer, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.employee_expenses_tag.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.employee_expenses_tag.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.employee_expenses_tag.expense_id</code>.
     */
    public void setExpenseId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.employee_expenses_tag.expense_id</code>.
     */
    public Integer getExpenseId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.employee_expenses_tag.tag_id</code>.
     */
    public void setTagId(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.employee_expenses_tag.tag_id</code>.
     */
    public Integer getTagId() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>public.employee_expenses_tag.created_at</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.employee_expenses_tag.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, Integer, Integer, LocalDateTime> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Long, Integer, Integer, LocalDateTime> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return EmployeeExpensesTag.EMPLOYEE_EXPENSES_TAG.ID;
    }

    @Override
    public Field<Integer> field2() {
        return EmployeeExpensesTag.EMPLOYEE_EXPENSES_TAG.EXPENSE_ID;
    }

    @Override
    public Field<Integer> field3() {
        return EmployeeExpensesTag.EMPLOYEE_EXPENSES_TAG.TAG_ID;
    }

    @Override
    public Field<LocalDateTime> field4() {
        return EmployeeExpensesTag.EMPLOYEE_EXPENSES_TAG.CREATED_AT;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Integer component2() {
        return getExpenseId();
    }

    @Override
    public Integer component3() {
        return getTagId();
    }

    @Override
    public LocalDateTime component4() {
        return getCreatedAt();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Integer value2() {
        return getExpenseId();
    }

    @Override
    public Integer value3() {
        return getTagId();
    }

    @Override
    public LocalDateTime value4() {
        return getCreatedAt();
    }

    @Override
    public EmployeeExpensesTagRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public EmployeeExpensesTagRecord value2(Integer value) {
        setExpenseId(value);
        return this;
    }

    @Override
    public EmployeeExpensesTagRecord value3(Integer value) {
        setTagId(value);
        return this;
    }

    @Override
    public EmployeeExpensesTagRecord value4(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public EmployeeExpensesTagRecord values(Long value1, Integer value2, Integer value3, LocalDateTime value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EmployeeExpensesTagRecord
     */
    public EmployeeExpensesTagRecord() {
        super(EmployeeExpensesTag.EMPLOYEE_EXPENSES_TAG);
    }

    /**
     * Create a detached, initialised EmployeeExpensesTagRecord
     */
    public EmployeeExpensesTagRecord(Long id, Integer expenseId, Integer tagId, LocalDateTime createdAt) {
        super(EmployeeExpensesTag.EMPLOYEE_EXPENSES_TAG);

        setId(id);
        setExpenseId(expenseId);
        setTagId(tagId);
        setCreatedAt(createdAt);
    }
}
