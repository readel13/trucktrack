ALTER TABLE work_trip_salary_history
    DROP CONSTRAINT fk_salary_history_salary_type,
    DROP COLUMN salary_type;

ALTER TABLE work_trip
    ADD COLUMN salary_type int NOT NULL DEFAULT 1, -- default HOURLY
    ADD CONSTRAINT fk_work_trip_salary_type FOREIGN KEY (salary_type) REFERENCES salary_type (id);