CREATE TABLE IF NOT EXISTS truck
(
    id               BIGSERIAL PRIMARY KEY,
    employee_id      int         NOT NULL,
    name             varchar(64) NOT NULL,
    truck_number     varchar(64),
    vin_code         varchar(64) NOT NULL,
    fuel_consumption NUMERIC(4, 2),
    active           boolean     NOT NULL default true,
    created_at       timestamp   NOT NULL default current_timestamp,

    constraint fk_truck_employee_id FOREIGN KEY (employee_id) REFERENCES employee (id)
);

CREATE INDEX idx_truck_name ON truck (name);
CREATE UNIQUE INDEX idx_truck_vin_code ON truck (vin_code);