CREATE TABLE IF NOT EXISTS company
(
    id          BIGSERIAL PRIMARY KEY,
    name        varchar(64)  NOT NULL,
    email       varchar(128) NOT NULL,
    description varchar(1024),
    url         varchar(1024),
    address     varchar(128),
    zipcode     varchar(32),
    created_at  timestamp    NOT NULL default current_timestamp
);