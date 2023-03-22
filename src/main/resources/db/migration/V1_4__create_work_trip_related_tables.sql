CREATE TABLE IF NOT EXISTS work_trip
(
    id          BIGSERIAL PRIMARY KEY,
    name        varchar(64),
    description varchar(128),
    employee_id int       NOT NULL,
    truck_id    int       NOT NULL,
    salary      int,
    currency_id int,
    active      boolean   NOT NULL default true,
    closed_at   timestamp,
    created_at  timestamp NOT NULL default current_timestamp,

    constraint fk_work_trip_employee_id FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE,
    constraint fk_work_trip_currency_id FOREIGN KEY (truck_id) REFERENCES currency (id),
    constraint fk_work_trip_truck_id FOREIGN KEY (truck_id) REFERENCES truck (id) ON DELETE CASCADE
);
CREATE INDEX idx_work_trip_name ON work_trip (name);
CREATE INDEX idx_work_trip_salary ON work_trip (salary);
CREATE INDEX idx_work_trip_closed_at ON work_trip (closed_at);


CREATE TABLE IF NOT EXISTS salary_type
(
    id   BIGSERIAL PRIMARY KEY,
    name varchar(16) NOT NULL
);

INSERT INTO salary_type(id, name)
VALUES (1, 'HOURLY'),
       (2, 'PER_KM');

CREATE TABLE IF NOT EXISTS work_trip_salary_history
(
    id                BIGSERIAL PRIMARY KEY,
    trip_id           int       NOT NULL,
    salary_type       int       NOT NULL,
    salary_rate       real      NOT NULL,
    calculation_value int       NOT NULL,
    created_at        timestamp NOT NULL default current_timestamp,

    constraint fk_salary_history_trip_id FOREIGN KEY (trip_id) REFERENCES work_trip (id) ON DELETE CASCADE,
    constraint fk_salary_history_salary_type FOREIGN KEY (salary_type) REFERENCES salary_type (id)
);

CREATE TABLE IF NOT EXISTS work_trip_cargo_history
(
    id                BIGSERIAL PRIMARY KEY,
    trip_id           int       NOT NULL,
    cargo_number      varchar(64),
    cargo_name        varchar(32),
    cargo_description varchar(32),
    cargo_weight      int,
    distance          int,
    delivered_at      timestamp,
    created_at        timestamp NOT NULL default current_timestamp,

    constraint fk_cargo_history_trip_id FOREIGN KEY (trip_id) REFERENCES work_trip (id) ON DELETE CASCADE
);