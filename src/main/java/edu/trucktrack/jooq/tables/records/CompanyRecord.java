/*
 * This file is generated by jOOQ.
 */
package edu.trucktrack.jooq.tables.records;


import edu.trucktrack.jooq.tables.Company;

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
public class CompanyRecord extends UpdatableRecordImpl<CompanyRecord> implements Record8<Long, String, String, String, String, String, String, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.company.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.company.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.company.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.company.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.company.email</code>.
     */
    public void setEmail(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.company.email</code>.
     */
    public String getEmail() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.company.description</code>.
     */
    public void setDescription(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.company.description</code>.
     */
    public String getDescription() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.company.url</code>.
     */
    public void setUrl(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.company.url</code>.
     */
    public String getUrl() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.company.address</code>.
     */
    public void setAddress(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.company.address</code>.
     */
    public String getAddress() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.company.zipcode</code>.
     */
    public void setZipcode(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.company.zipcode</code>.
     */
    public String getZipcode() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.company.created_at</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.company.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(7);
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
    public Row8<Long, String, String, String, String, String, String, LocalDateTime> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<Long, String, String, String, String, String, String, LocalDateTime> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Company.COMPANY.ID;
    }

    @Override
    public Field<String> field2() {
        return Company.COMPANY.NAME;
    }

    @Override
    public Field<String> field3() {
        return Company.COMPANY.EMAIL;
    }

    @Override
    public Field<String> field4() {
        return Company.COMPANY.DESCRIPTION;
    }

    @Override
    public Field<String> field5() {
        return Company.COMPANY.URL;
    }

    @Override
    public Field<String> field6() {
        return Company.COMPANY.ADDRESS;
    }

    @Override
    public Field<String> field7() {
        return Company.COMPANY.ZIPCODE;
    }

    @Override
    public Field<LocalDateTime> field8() {
        return Company.COMPANY.CREATED_AT;
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
        return getEmail();
    }

    @Override
    public String component4() {
        return getDescription();
    }

    @Override
    public String component5() {
        return getUrl();
    }

    @Override
    public String component6() {
        return getAddress();
    }

    @Override
    public String component7() {
        return getZipcode();
    }

    @Override
    public LocalDateTime component8() {
        return getCreatedAt();
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
        return getEmail();
    }

    @Override
    public String value4() {
        return getDescription();
    }

    @Override
    public String value5() {
        return getUrl();
    }

    @Override
    public String value6() {
        return getAddress();
    }

    @Override
    public String value7() {
        return getZipcode();
    }

    @Override
    public LocalDateTime value8() {
        return getCreatedAt();
    }

    @Override
    public CompanyRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public CompanyRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public CompanyRecord value3(String value) {
        setEmail(value);
        return this;
    }

    @Override
    public CompanyRecord value4(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public CompanyRecord value5(String value) {
        setUrl(value);
        return this;
    }

    @Override
    public CompanyRecord value6(String value) {
        setAddress(value);
        return this;
    }

    @Override
    public CompanyRecord value7(String value) {
        setZipcode(value);
        return this;
    }

    @Override
    public CompanyRecord value8(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public CompanyRecord values(Long value1, String value2, String value3, String value4, String value5, String value6, String value7, LocalDateTime value8) {
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
     * Create a detached CompanyRecord
     */
    public CompanyRecord() {
        super(Company.COMPANY);
    }

    /**
     * Create a detached, initialised CompanyRecord
     */
    public CompanyRecord(Long id, String name, String email, String description, String url, String address, String zipcode, LocalDateTime createdAt) {
        super(Company.COMPANY);

        setId(id);
        setName(name);
        setEmail(email);
        setDescription(description);
        setUrl(url);
        setAddress(address);
        setZipcode(zipcode);
        setCreatedAt(createdAt);
    }
}