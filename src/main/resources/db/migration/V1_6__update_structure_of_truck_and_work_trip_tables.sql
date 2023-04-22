ALTER TABLE truck
    DROP CONSTRAINT fk_truck_employee_id,
    DROP COLUMN employee_id;

ALTER TABLE truck
    ADD COLUMN company_id int DEFAULT NULL,
    ADD CONSTRAINT fk_truck_company_id FOREIGN KEY (company_id) REFERENCES company (id);