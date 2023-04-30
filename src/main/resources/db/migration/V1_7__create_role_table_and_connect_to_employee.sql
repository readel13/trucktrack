create table roles
(
    id   serial,
    role varchar(30)
);

create table employee_roles
(
    employee_id integer,
    role_id     integer
);

INSERT INTO roles (id, role) VALUES (1, 'DRIVER');
INSERT INTO roles (id, role) VALUES (2, 'OWNER');
INSERT INTO roles (id, role) VALUES (3, 'MANAGER');


