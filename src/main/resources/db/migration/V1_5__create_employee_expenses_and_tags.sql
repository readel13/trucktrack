CREATE TABLE IF NOT EXISTS employee_expenses
(
    id          BIGSERIAL PRIMARY KEY,
    employee_id int       NOT NULL,
    trip_id     int       NOT NULL,
    name        varchar(64),
    description varchar(128),
    value       int,
    currency_id int,
    created_at  timestamp NOT NULL default current_timestamp,

    constraint fk_employee_expenses_employee_id FOREIGN KEY (employee_id) REFERENCES employee (id) ON DELETE CASCADE,
    constraint fk_employee_expenses_trip_id FOREIGN KEY (trip_id) REFERENCES work_trip (id) ON DELETE CASCADE,
    constraint fk_employee_expenses_currency_id FOREIGN KEY (currency_id) REFERENCES currency (id)
);
CREATE INDEX idx_employee_expenses_name ON employee_expenses (name);
CREATE INDEX idx_employee_expenses_salary ON employee_expenses (value);

CREATE TABLE tag
(
    id                     BIGSERIAL PRIMARY KEY,
    name                   varchar(64),
    is_system              boolean            default false,
    created_by_employee_id int       NOT NULL,
    created_at             timestamp NOT NULL default current_timestamp,

    constraint fk_tag_created_by_employee_id FOREIGN KEY (created_by_employee_id) REFERENCES employee (id) ON DELETE CASCADE
);
CREATE INDEX idx_tag_name ON tag (name);


CREATE TABLE IF NOT EXISTS employee_expenses_tag
(
    id         BIGSERIAL PRIMARY KEY,
    expense_id INT       NOT NULL,
    tag_id     int       not null,
    created_at timestamp NOT NULL default current_timestamp,

    constraint fk_employee_expenses_tag_expense_id FOREIGN KEY (expense_id) REFERENCES employee_expenses (id) ON DELETE CASCADE,
    constraint fk_employee_expenses_tag_tag_id FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE CASCADE
);