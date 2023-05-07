ALTER TABLE work_trip
    DROP CONSTRAINT fk_work_trip_currency_id;

ALTER TABLE work_trip
    ADD CONSTRAINT fk_work_trip_currency_id FOREIGN KEY (currency_id) REFERENCES currency (id);