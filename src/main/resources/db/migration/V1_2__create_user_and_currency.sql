CREATE TABLE IF NOT EXISTS currency
(
    id   BIGSERIAL PRIMARY KEY,
    name varchar(16) NOT NULL
);

INSERT INTO currency(id, name)
VALUES (1, 'USD'),
       (2, 'UAH'),
       (3, 'EUR');

CREATE TABLE IF NOT EXISTS employee
(
    id           BIGSERIAL PRIMARY KEY,
    company_id   int                   DEFAULT NULL,
    name         varchar(64)  NOT NULL,
    email        varchar(128) NOT NULL,
    password     varchar(64)  NOT NULL,
    active       boolean      NOT NULL default true,
    phone_number varchar(64)           DEFAULT NULL,
    currency_id  int          NOT NULL DEFAULT 1, -- default USD currency
    created_at   timestamp    NOT NULL DEFAULT current_timestamp,

    constraint fk_employee_company_id FOREIGN KEY (company_id) REFERENCES company (id),
    constraint fk_employee_currency_id FOREIGN KEY (currency_id) REFERENCES currency (id)
);

CREATE INDEX idx_employee_name ON employee (name);