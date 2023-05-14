ALTER TABLE work_trip_salary_history
    DROP COLUMN salary_rate;

ALTER TABLE work_trip
    ADD COLUMN salary_rate REAL NOT NULL DEFAULT 1;